package com.hiresphere.hiresphere.JobSeeker.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Exception.JobSeekerProfileNotFoundException;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;
import com.hiresphere.hiresphere.JobSeeker.Mapper.JobSeekerMapper;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class JobSeekerServiceImpl
        implements JobSeekerService {


    private final UserRepository userRepository;

    private final JobSeekerRepository jobSeekerRepository;


    // =====================================================
    // CREATE PROFILE
    // =====================================================

    @Override
    public JobSeekerProfileResponseDto createProfile(
            JobSeekerProfileRequestDto dto) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"
                                )
                        );


        // Only Job Seeker can create profile
        if (user.getRole() != UserRoles.JOB_SEEKER) {

            throw new RuntimeException(
                    "Only Job Seeker Can Create Profile"
            );
        }


        // If profile already exists, update it instead of throwing an error
        if (jobSeekerRepository.existsByUser(user)) {
            return updateProfile(dto);
        }


        JobSeekerProfile profile =
                JobSeekerMapper
                        .mapToJobSeekerProfile(dto);


        // Attach logged-in user
        profile.setUser(user);


        JobSeekerProfile savedProfile =
                jobSeekerRepository.save(profile);


        return JobSeekerMapper
                .mapToJobSeekerProfileResponseDto(
                        savedProfile
                );
    }


    // =====================================================
    // GET PROFILE
    // =====================================================

    @Override
    public JobSeekerProfileResponseDto getProfile() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"
                                )
                        );


        // Only Job Seeker can access profile
        if (user.getRole() != UserRoles.JOB_SEEKER) {

            throw new RuntimeException(
                    "Only Job Seeker Allowed"
            );
        }


        JobSeekerProfile profile =
                jobSeekerRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new JobSeekerProfileNotFoundException(
                                        "Job Seeker Profile Not Found"
                                )
                        );


        return JobSeekerMapper
                .mapToJobSeekerProfileResponseDto(
                        profile
                );
    }


    // =====================================================
    // UPDATE PROFILE (UPSERT SAFE)
    // =====================================================

    @Override
    public JobSeekerProfileResponseDto updateProfile(
            JobSeekerProfileRequestDto dto) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"
                                )
                        );


        // Only Job Seeker can update profile
        if (user.getRole() != UserRoles.JOB_SEEKER) {

            throw new RuntimeException(
                    "Only Job Seeker Allowed"
            );
        }


        JobSeekerProfile profile =
                jobSeekerRepository
                        .findByUser(user)
                        .orElseGet(() -> {
                            JobSeekerProfile newProfile = JobSeekerMapper.mapToJobSeekerProfile(dto);
                            newProfile.setUser(user);
                            return newProfile;
                        });


        // Update fields

        profile.setFullName(
                dto.getFullName()
        );

        profile.setGender(
                dto.getGender()
        );

        profile.setPhoneNumber(
                dto.getPhoneNumber()
        );

        profile.setLocation(
                dto.getLocation()
        );

        profile.setHeadline(
                dto.getHeadline()
        );

        profile.setExperience(
                dto.getExperience()
        );

        profile.setSkills(
                dto.getSkills()
        );

        profile.setSummary(
                dto.getSummary()
        );

        profile.setHighestQualification(
                dto.getHighestQualification()
        );

        profile.setCollegeName(
                dto.getCollegeName()
        );

        if (dto.getCourse() != null) {
            profile.setCourse(dto.getCourse());
        }

        if (dto.getPassingYear() != null) {
            profile.setPassingYear(dto.getPassingYear());
        }

        if (dto.getCurrentDesignation() != null) {
            profile.setCurrentDesignation(dto.getCurrentDesignation());
        }

        if (dto.getCurrentCompany() != null) {
            profile.setCurrentCompany(dto.getCurrentCompany());
        }

        if (dto.getCurrentSalary() != null) {
            profile.setCurrentSalary(dto.getCurrentSalary());
        }

        if (dto.getExpectedSalary() != null) {
            profile.setExpectedSalary(dto.getExpectedSalary());
        }

        if (dto.getPreferredLocation() != null) {
            profile.setPreferredLocation(dto.getPreferredLocation());
        }

        if (dto.getResumeFileName() != null && !dto.getResumeFileName().trim().isEmpty()) {
            profile.setResumeFileName(dto.getResumeFileName());
        }

        if (dto.getResumeUrl() != null && !dto.getResumeUrl().trim().isEmpty()) {
            profile.setResumeUrl(dto.getResumeUrl());
        }

        if (dto.getNoticePeriod() != null) {
            profile.setNoticePeriod(dto.getNoticePeriod());
        }

        if (dto.getGithubUrl() != null) {
            profile.setGithubUrl(dto.getGithubUrl());
        }

        if (dto.getLinkedinUrl() != null) {
            profile.setLinkedinUrl(dto.getLinkedinUrl());
        }

        if (dto.getPortfolioUrl() != null) {
            profile.setPortfolioUrl(dto.getPortfolioUrl());
        }

        // Naukri Career Profile Fields
        if (dto.getCurrentIndustry() != null) {
            profile.setCurrentIndustry(dto.getCurrentIndustry());
        }
        if (dto.getDepartment() != null) {
            profile.setDepartment(dto.getDepartment());
        }
        if (dto.getRoleCategory() != null) {
            profile.setRoleCategory(dto.getRoleCategory());
        }
        if (dto.getDesiredJobType() != null) {
            profile.setDesiredJobType(dto.getDesiredJobType());
        }
        if (dto.getDesiredEmploymentType() != null) {
            profile.setDesiredEmploymentType(dto.getDesiredEmploymentType());
        }
        if (dto.getPreferredWorkMode() != null) {
            profile.setPreferredWorkMode(dto.getPreferredWorkMode());
        }
        if (dto.getPreferredShift() != null) {
            profile.setPreferredShift(dto.getPreferredShift());
        }

        // Naukri Personal Details Fields
        if (dto.getDateOfBirth() != null) {
            profile.setDateOfBirth(dto.getDateOfBirth());
        }
        if (dto.getMaritalStatus() != null) {
            profile.setMaritalStatus(dto.getMaritalStatus());
        }
        if (dto.getHometown() != null) {
            profile.setHometown(dto.getHometown());
        }
        if (dto.getPincode() != null) {
            profile.setPincode(dto.getPincode());
        }
        if (dto.getPermanentAddress() != null) {
            profile.setPermanentAddress(dto.getPermanentAddress());
        }
        if (dto.getLanguagesKnown() != null) {
            profile.setLanguagesKnown(dto.getLanguagesKnown());
        }
        if (dto.getDifferentlyAbled() != null) {
            profile.setDifferentlyAbled(dto.getDifferentlyAbled());
        }
        if (dto.getCareerBreak() != null) {
            profile.setCareerBreak(dto.getCareerBreak());
        }

        JobSeekerProfile updatedProfile =
                jobSeekerRepository.save(profile);


        return JobSeekerMapper
                .mapToJobSeekerProfileResponseDto(
                        updatedProfile
                );
    }


    // =====================================================
    // DELETE PROFILE
    // =====================================================

    @Override
    public void deleteProfile() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"
                                )
                        );


        // Only Job Seeker can delete profile
        if (user.getRole() != UserRoles.JOB_SEEKER) {

            throw new RuntimeException(
                    "Only Job Seeker Allowed"
            );
        }


        JobSeekerProfile profile =
                jobSeekerRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new JobSeekerProfileNotFoundException(
                                        "Job Seeker Profile Not Found"
                                )
                        );


        jobSeekerRepository.delete(profile);
    }

    // =====================================================
    // RECRUITER TALENT SEARCH (NAUKRI RESDEX STYLE)
    // =====================================================

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public java.util.List<JobSeekerProfileResponseDto> searchCandidates(
            String skill,
            String location,
            Integer minExp,
            Integer maxExp,
            String company,
            String designation,
            String noticePeriod,
            String workMode,
            Double maxExpectedSalary
    ) {
        return jobSeekerRepository.findAll(
                com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerSpecification.searchCandidates(
                        skill,
                        location,
                        minExp,
                        maxExp,
                        company,
                        designation,
                        noticePeriod,
                        workMode,
                        maxExpectedSalary
                )
        ).stream()
                .map(JobSeekerMapper::mapToJobSeekerProfileResponseDto)
                .toList();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public JobSeekerProfileResponseDto getCandidateProfileById(Long candidateProfileId) {
        JobSeekerProfile profile = jobSeekerRepository.findById(candidateProfileId)
                .orElseThrow(() -> new JobSeekerProfileNotFoundException(
                        "Candidate profile not found with ID: " + candidateProfileId
                ));
        return JobSeekerMapper.mapToJobSeekerProfileResponseDto(profile);
    }
}