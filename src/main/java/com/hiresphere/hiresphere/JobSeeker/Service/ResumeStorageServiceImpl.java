package com.hiresphere.hiresphere.JobSeeker.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Exception.JobSeekerProfileNotFoundException;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;
import com.hiresphere.hiresphere.JobSeeker.Mapper.JobSeekerMapper;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerRepository;

import com.hiresphere.hiresphere.Application.Repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeStorageServiceImpl implements ResumeStorageService {

    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final ApplicationRepository applicationRepository;
    private final com.hiresphere.hiresphere.Config.CloudinaryService cloudinaryService;

    @Value("${app.upload.dir:uploads/resumes}")
    private String uploadDir;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public JobSeekerProfileResponseDto uploadResume(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select a PDF file to upload.");
        }

        // Fast Validate File Type (PDF)
        String contentType = file.getContentType();
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "resume.pdf");
        if ((contentType != null && !contentType.equalsIgnoreCase("application/pdf")) &&
            !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        // Validate Max File Size (10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must not exceed 10MB.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (user.getRole() != UserRoles.JOB_SEEKER) {
            throw new RuntimeException("Only Job Seekers can upload resumes.");
        }

        JobSeekerProfile profile = jobSeekerRepository.findByUser(user)
                .orElseGet(() -> {
                    JobSeekerProfile base = new JobSeekerProfile();
                    base.setUser(user);
                    base.setFullName(user.getName() != null && !user.getName().isBlank() ? user.getName() : "Candidate");
                    base.setGender(com.hiresphere.hiresphere.JobSeeker.Enums.Gender.MALE);
                    base.setPhoneNumber("9000000000");
                    base.setLocation("Pune");
                    base.setHeadline("Software Engineering Candidate");
                    base.setExperience(0);
                    base.setSkills("Java, Spring Boot, React.js, SQL");
                    base.setHighestQualification(com.hiresphere.hiresphere.JobSeeker.Enums.Qualification.BTECH);
                    base.setCollegeName("Savitribai Phule Pune University (SPPU)");
                    return jobSeekerRepository.save(base);
                });

        String finalResumeUrl = null;

        // 1. Try Cloudinary if configured (with 6s timeout protection to prevent hanging)
        if (cloudinaryService != null && cloudinaryService.isConfigured()) {
            try {
                String publicId = "resume_user_" + user.getUserId() + "_" + UUID.randomUUID().toString();
                finalResumeUrl = cloudinaryService.uploadResumeWithTimeout(file, publicId, 6);
                log.info("[RESUME UPLOAD] Successfully stored in Cloudinary for user ID: {}", user.getUserId());
            } catch (Exception e) {
                log.warn("[RESUME UPLOAD] Cloudinary upload delayed or failed ({}). Gracefully falling back to local disk storage to avoid hanging.", e.getMessage());
            }
        }

        // 2. Fallback: Local disk storage (guaranteed ultra-fast, local, non-blocking)
        if (finalResumeUrl == null) {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Clean previous local file ONLY if not referenced in any submitted job application
            String previousUrl = profile.getResumeUrl();
            if (previousUrl != null && previousUrl.startsWith("/jobseeker/resume/download/")) {
                boolean isUsedInApplications = applicationRepository.existsByAppliedResumeUrl(previousUrl);
                if (!isUsedInApplications) {
                    String oldFileName = previousUrl.substring("/jobseeker/resume/download/".length());
                    try {
                        Path oldFilePath = uploadPath.resolve(oldFileName).normalize();
                        Files.deleteIfExists(oldFilePath);
                    } catch (Exception ignored) {
                    }
                }
            }

            // Generate unique filename with UUID
            String fileExtension = ".pdf";
            String uniqueFileName = "resume_user_" + user.getUserId() + "_" + UUID.randomUUID().toString() + fileExtension;
            Path targetLocation = uploadPath.resolve(uniqueFileName);

            // Copy file to disk
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            finalResumeUrl = "/jobseeker/resume/download/" + uniqueFileName;
            log.info("[RESUME UPLOAD] Successfully saved locally at {} for user ID: {}", targetLocation, user.getUserId());
        }

        // 3. Atomically persist updated profile metadata
        profile.setUser(user);
        profile.setResumeUrl(finalResumeUrl);
        profile.setResumeFileName(originalFilename);
        JobSeekerProfile updatedProfile = jobSeekerRepository.save(profile);
        return JobSeekerMapper.mapToJobSeekerProfileResponseDto(updatedProfile);
    }

    @Override
    public Path getResumeFilePath(String fileName) {
        if (fileName == null || fileName.isBlank() || fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new SecurityException("Invalid file name: path traversal characters detected");
        }
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path resolved = uploadPath.resolve(fileName).normalize();
        if (!resolved.startsWith(uploadPath)) {
            throw new SecurityException("Path traversal attempt detected");
        }
        return resolved;
    }
}
