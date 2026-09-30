package com.hiresphere.hiresphere.Application.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiresphere.hiresphere.Application.DTO.ApplicantResponseDto;
import com.hiresphere.hiresphere.Application.DTO.ApplyJobRequestDto;
import com.hiresphere.hiresphere.Application.DTO.MyApplicationResponseDto;
import com.hiresphere.hiresphere.Application.Entity.Application;
import com.hiresphere.hiresphere.Application.Enums.ApplicationStatus;
import com.hiresphere.hiresphere.Application.Mapper.ApplicationMapper;
import com.hiresphere.hiresphere.Application.Repository.ApplicationRepository;
import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Exception.BadRequestException;
import com.hiresphere.hiresphere.Exception.DuplicateResourceException;
import com.hiresphere.hiresphere.Exception.RecruiterProfileNotFoundException;
import com.hiresphere.hiresphere.Exception.ResourceNotFoundException;
import com.hiresphere.hiresphere.Exception.UnauthorizedOperationException;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Job.Repository.JobRepository;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerRepository;
import com.hiresphere.hiresphere.Notification.Service.NotificationService;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;
import com.hiresphere.hiresphere.Recruiter.Repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final RecruiterRepository recruiterRepository;
    private final NotificationService notificationService;

    // Helper: get authenticated user
    private Users getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedOperationException("Authentication required");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    // Helper: get authenticated recruiter profile
    private RecruiterProfile getAuthenticatedRecruiter() {
        Users user = getAuthenticatedUser();
        if (user.getRole() != UserRoles.RECRUITER) {
            throw new UnauthorizedOperationException("Only recruiter allowed to perform this operation");
        }

        return recruiterRepository.findByUser(user)
                .orElseThrow(() -> new RecruiterProfileNotFoundException("Recruiter profile not found. Please complete your recruiter profile first."));
    }

    // ==================================================
    // 1 Apply Job
    // ==================================================
    @Override
    public MyApplicationResponseDto applyJob(Long jobId, ApplyJobRequestDto dto) {
        Users user = getAuthenticatedUser();

        if (user.getRole() != UserRoles.JOB_SEEKER) {
            throw new UnauthorizedOperationException("Only Job Seeker can apply for jobs");
        }

        JobSeekerProfile profile = jobSeekerRepository.findByUser(user)
                .orElseGet(() -> {
                    JobSeekerProfile starter = new JobSeekerProfile();
                    starter.setUser(user);
                    starter.setFullName(user.getName() != null && !user.getName().isBlank() ? user.getName() : "Candidate");
                    starter.setGender(com.hiresphere.hiresphere.JobSeeker.Enums.Gender.MALE);
                    starter.setPhoneNumber("9000000000");
                    starter.setLocation("Pune");
                    starter.setHeadline("Software Engineering Candidate");
                    starter.setExperience(0);
                    starter.setSkills("Java, Spring Boot, SQL");
                    starter.setHighestQualification(com.hiresphere.hiresphere.JobSeeker.Enums.Qualification.BTECH);
                    starter.setCollegeName("Savitribai Phule Pune University (SPPU)");
                    return jobSeekerRepository.save(starter);
                });

        Job job = jobRepository.findByIdWithRecruiter(jobId)
                .orElseGet(() -> jobRepository.findById(jobId)
                        .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId)));

        if (job.getStatus() != JobStatus.OPEN) {
            throw new BadRequestException("Job is not accepting applications");
        }

        if (job.getApplicationDeadline() != null && job.getApplicationDeadline().isBefore(LocalDate.now())) {
            throw new BadRequestException("Application deadline for this job has expired");
        }

        if (applicationRepository.existsByJobAndJobSeekerProfile(job, profile)) {
            throw new DuplicateResourceException("You have already applied for this job");
        }

        Application application = new Application();
        application.setJob(job);
        application.setJobSeekerProfile(profile);
        application.setCoverLetter(dto != null ? dto.getCoverLetter() : null);
        application.setAppliedResumeUrl(profile.getResumeUrl() != null ? profile.getResumeUrl() : "");
        application.setStatus(ApplicationStatus.APPLIED);

        Application savedApplication;
        try {
            savedApplication = applicationRepository.save(application);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Concurrent duplicate application prevented for user {} on job {}", user.getEmail(), jobId);
            throw new DuplicateResourceException("You have already applied for this job");
        }

        log.info("Application submitted: id={}, job={}, candidate={}", savedApplication.getId(), job.getTitle(), profile.getFullName());

        String companyName = (job.getRecruiterProfile() != null && job.getRecruiterProfile().getCompanyName() != null)
                ? job.getRecruiterProfile().getCompanyName()
                : "Employer";

        // 1. Notify the Job's specific Recruiter about new application
        try {
            if (job.getRecruiterProfile() != null && job.getRecruiterProfile().getUser() != null) {
                notificationService.createNotification(
                        job.getRecruiterProfile().getUser(),
                        "New Application Received",
                        profile.getFullName() + " applied for " + job.getTitle(),
                        "NEW_APPLICATION",
                        savedApplication.getId()
                );
            }
        } catch (Exception e) {
            log.warn("Could not create recruiter notification: {}", e.getMessage());
        }

        // 2. Notify the Job Seeker that their application was successfully delivered
        try {
            notificationService.createNotification(
                    user,
                    "Application Submitted",
                    "You have successfully applied for " + job.getTitle() + " at " + companyName + ".",
                    "APPLICATION_SUBMITTED",
                    savedApplication.getId()
            );
        } catch (Exception e) {
            log.warn("Could not create candidate notification: {}", e.getMessage());
        }

        return ApplicationMapper.mapToMyApplicationResponseDto(savedApplication);
    }

    // ==================================================
    // 2 Get My Applications
    // ==================================================
    @Override
    @Transactional(readOnly = true)
    public List<MyApplicationResponseDto> getMyApplications() {
        Users user = getAuthenticatedUser();

        if (user.getRole() != UserRoles.JOB_SEEKER) {
            throw new UnauthorizedOperationException("Only Job Seeker can view applications");
        }

        var profileOpt = jobSeekerRepository.findByUser(user);
        if (profileOpt.isEmpty()) {
            return new ArrayList<>();
        }

        List<Application> applications = applicationRepository.findByJobSeekerProfile(profileOpt.get());
        return applications.stream()
                .map(ApplicationMapper::mapToMyApplicationResponseDto)
                .toList();
    }

    // ==================================================
    // 3 Withdraw Application
    // ==================================================
    @Override
    public void withdrawApplication(Long applicationId) {
        Users user = getAuthenticatedUser();

        if (user.getRole() != UserRoles.JOB_SEEKER) {
            throw new UnauthorizedOperationException("Only Job Seeker can withdraw applications");
        }

        JobSeekerProfile profile = jobSeekerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Job Seeker Profile not found"));

        Application application = applicationRepository.findByIdAndJobSeekerProfile(applicationId, profile)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        if (application.getStatus() == ApplicationStatus.ACCEPTED || application.getStatus() == ApplicationStatus.HIRED) {
            throw new BadRequestException("Accepted application cannot be withdrawn");
        }

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new BadRequestException("Application is already withdrawn");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        applicationRepository.save(application);
        log.info("Application withdrawn: id={}", applicationId);
    }

    // ==================================================
    // 4 Recruiter: Get Applicants for Job
    // ==================================================
    @Override
    @Transactional(readOnly = true)
    public List<ApplicantResponseDto> getApplicantsForJob(Long jobId) {
        RecruiterProfile recruiterProfile = getAuthenticatedRecruiter();

        Job job = jobRepository.findByIdWithRecruiter(jobId)
                .orElseGet(() -> jobRepository.findById(jobId)
                        .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId)));

        if (!job.getRecruiterProfile().getRecruiter_Profile_id().equals(recruiterProfile.getRecruiter_Profile_id())) {
            log.warn("Security violation: Recruiter ID {} attempted to view applicants for Job ID {} owned by Recruiter ID {}",
                    recruiterProfile.getRecruiter_Profile_id(), jobId, job.getRecruiterProfile().getRecruiter_Profile_id());
            throw new UnauthorizedOperationException("You can only view applicants for your own jobs");
        }

        List<Application> applications = applicationRepository.findByJob(job);
        return applications.stream()
                .map(ApplicationMapper::mapToApplicantResponseDto)
                .toList();
    }

    // ==================================================
    // 5 Get Application By ID (Strict Bidirectional Ownership Verification)
    // ==================================================
    @Override
    @Transactional(readOnly = true)
    public ApplicantResponseDto getApplicationById(Long applicationId) {
        Users currentUser = getAuthenticatedUser();

        Application application = applicationRepository.findByIdWithDetails(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        // Ownership Verification:
        if (currentUser.getRole() == UserRoles.JOB_SEEKER) {
            // 1. Candidate can view ONLY their own application
            JobSeekerProfile seekerProfile = application.getJobSeekerProfile();
            if (seekerProfile == null || seekerProfile.getUser() == null || !seekerProfile.getUser().getUserId().equals(currentUser.getUserId())) {
                log.warn("Security violation: Candidate ID {} attempted to access Application ID {} belonging to someone else",
                        currentUser.getUserId(), applicationId);
                throw new UnauthorizedOperationException("You can view only your own applications");
            }
        } else if (currentUser.getRole() == UserRoles.RECRUITER) {
            // 2. Recruiter can view ONLY applicants for their own jobs
            RecruiterProfile recruiterProfile = application.getJob().getRecruiterProfile();
            if (recruiterProfile == null || recruiterProfile.getUser() == null || !recruiterProfile.getUser().getUserId().equals(currentUser.getUserId())) {
                log.warn("Security violation: Recruiter ID {} attempted to access Application ID {} belonging to someone else",
                        currentUser.getUserId(), applicationId);
                throw new UnauthorizedOperationException("You can view only your own applicants");
            }
        } else if (currentUser.getRole() != UserRoles.ADMIN) {
            throw new UnauthorizedOperationException("Unauthorized to view this application");
        }

        return ApplicationMapper.mapToApplicantResponseDto(application);
    }

    // ==================================================
    // 6 Recruiter: Shortlist Application
    // ==================================================
    @Override
    public ApplicantResponseDto shortlistApplication(Long applicationId) {
        return updateStatusInternal(applicationId, ApplicationStatus.SHORTLISTED);
    }

    // ==================================================
    // 7 Recruiter: Accept Application
    // ==================================================
    @Override
    public ApplicantResponseDto acceptApplication(Long applicationId) {
        return updateStatusInternal(applicationId, ApplicationStatus.ACCEPTED);
    }

    // ==================================================
    // 8 Recruiter: Reject Application
    // ==================================================
    @Override
    public ApplicantResponseDto rejectApplication(Long applicationId) {
        return updateStatusInternal(applicationId, ApplicationStatus.REJECTED);
    }

    // ==================================================
    // 9 Recruiter: Update Application Status
    // ==================================================
    @Override
    public ApplicantResponseDto updateApplicationStatus(Long applicationId, ApplicationStatus newStatus) {
        return updateStatusInternal(applicationId, newStatus);
    }

    // Common status update implementation with ownership check & candidate notification
    private ApplicantResponseDto updateStatusInternal(Long applicationId, ApplicationStatus newStatus) {
        RecruiterProfile recruiterProfile = getAuthenticatedRecruiter();

        Application application = applicationRepository.findByIdWithDetails(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        if (!application.getJob().getRecruiterProfile().getRecruiter_Profile_id().equals(recruiterProfile.getRecruiter_Profile_id())) {
            log.warn("Security violation: Recruiter ID {} attempted to update Application ID {} owned by Recruiter ID {}",
                    recruiterProfile.getRecruiter_Profile_id(), applicationId, application.getJob().getRecruiterProfile().getRecruiter_Profile_id());
            throw new UnauthorizedOperationException("You can update only your own applicants");
        }

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
            throw new BadRequestException("Withdrawn application status cannot be modified");
        }

        application.setStatus(newStatus);
        Application updated = applicationRepository.save(application);
        log.info("Application {} status updated to {} by recruiter {}", applicationId, newStatus, recruiterProfile.getRecruiter_Profile_id());

        // Notify Candidate
        try {
            Users candidateUser = application.getJobSeekerProfile().getUser();
            if (candidateUser != null) {
                String title;
                String desc;
                String type;

                if (newStatus == ApplicationStatus.SHORTLISTED) {
                    title = "Application Shortlisted!";
                    desc = "Congratulations! Your application for " + application.getJob().getTitle() + " at " + application.getJob().getRecruiterProfile().getCompanyName() + " has been shortlisted.";
                    type = "APPLICATION_SHORTLISTED";
                } else if (newStatus == ApplicationStatus.ACCEPTED || newStatus == ApplicationStatus.HIRED || newStatus == ApplicationStatus.OFFERED) {
                    title = "Application Offer / Accepted!";
                    desc = "Great news! Your application for " + application.getJob().getTitle() + " at " + application.getJob().getRecruiterProfile().getCompanyName() + " has been accepted.";
                    type = "APPLICATION_ACCEPTED";
                } else if (newStatus == ApplicationStatus.REJECTED) {
                    title = "Application Status Update";
                    desc = "Your application for " + application.getJob().getTitle() + " at " + application.getJob().getRecruiterProfile().getCompanyName() + " was not selected at this time.";
                    type = "APPLICATION_REJECTED";
                } else {
                    title = "Application Status: " + newStatus;
                    desc = "Your application for " + application.getJob().getTitle() + " at " + application.getJob().getRecruiterProfile().getCompanyName() + " has been updated to " + newStatus + ".";
                    type = "APPLICATION_STATUS_UPDATE";
                }

                notificationService.createNotification(candidateUser, title, desc, type, application.getId());
            }
        } catch (Exception e) {
            log.debug("Non-critical notification error: {}", e.getMessage());
        }

        return ApplicationMapper.mapToApplicantResponseDto(updated);
    }
}