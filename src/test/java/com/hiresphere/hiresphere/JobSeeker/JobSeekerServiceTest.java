package com.hiresphere.hiresphere.JobSeeker;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Exception.JobSeekerProfileNotFoundException;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;
import com.hiresphere.hiresphere.JobSeeker.Enums.Gender;
import com.hiresphere.hiresphere.JobSeeker.Enums.Qualification;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerRepository;
import com.hiresphere.hiresphere.JobSeeker.Service.JobSeekerServiceImpl;

@ExtendWith(MockitoExtension.class)
class JobSeekerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobSeekerRepository jobSeekerRepository;

    @InjectMocks
    private JobSeekerServiceImpl jobSeekerService;

    private Users jobSeekerUser;
    private JobSeekerProfile profile;

    @BeforeEach
    void setUp() {
        jobSeekerUser = new Users();
        jobSeekerUser.setUserId(2L);
        jobSeekerUser.setEmail("developer@gmail.com");
        jobSeekerUser.setRole(UserRoles.JOB_SEEKER);

        profile = new JobSeekerProfile();
        profile.setJobSeekerProfileId(20L);
        profile.setFullName("Aditya Patil");
        profile.setGender(Gender.MALE);
        profile.setPhoneNumber("9876543210");
        profile.setLocation("Pune");
        profile.setHeadline("Full Stack Java Developer");
        profile.setExperience(3);
        profile.setSkills("Java, Spring Boot, React, MySQL");
        profile.setSummary("Passionate developer with 3 yrs experience");
        profile.setHighestQualification(Qualification.BTECH);
        profile.setCollegeName("COEP Pune");
        profile.setResumeUrl(null); // Optional initially
        profile.setNoticePeriod("30_DAYS");
        profile.setGithubUrl("https://github.com/aditya");
        profile.setUser(jobSeekerUser);
    }

    private void mockSecurityContext(String email) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should create Job Seeker profile without requiring resumeUrl")
    void testCreateProfile_WithoutResumeUrl_Success() {
        mockSecurityContext("developer@gmail.com");
        when(userRepository.findByEmail("developer@gmail.com")).thenReturn(Optional.of(jobSeekerUser));
        when(jobSeekerRepository.existsByUser(jobSeekerUser)).thenReturn(false);
        when(jobSeekerRepository.save(any(JobSeekerProfile.class))).thenReturn(profile);

        JobSeekerProfileRequestDto dto = new JobSeekerProfileRequestDto();
        dto.setFullName("Aditya Patil");
        dto.setGender(Gender.MALE);
        dto.setPhoneNumber("9876543210");
        dto.setLocation("Pune");
        dto.setHeadline("Full Stack Java Developer");
        dto.setExperience(3);
        dto.setSkills("Java, Spring Boot, React, MySQL");
        dto.setSummary("Passionate developer with 3 yrs experience");
        dto.setHighestQualification(Qualification.BTECH);
        dto.setCollegeName("COEP Pune");
        dto.setResumeUrl(null); // No resume URL provided initially
        dto.setNoticePeriod("30_DAYS");
        dto.setGithubUrl("https://github.com/aditya");

        JobSeekerProfileResponseDto result = jobSeekerService.createProfile(dto);

        assertNotNull(result);
        assertEquals("Aditya Patil", result.getFullName());
        assertEquals("30_DAYS", result.getNoticePeriod());
        assertEquals("https://github.com/aditya", result.getGithubUrl());
        verify(jobSeekerRepository, times(1)).save(any(JobSeekerProfile.class));
    }

    @Test
    @DisplayName("Should update profile when profile already exists for the user")
    void testCreateProfile_Duplicate_UpdatesProfile() {
        mockSecurityContext("developer@gmail.com");
        when(userRepository.findByEmail("developer@gmail.com")).thenReturn(Optional.of(jobSeekerUser));
        when(jobSeekerRepository.existsByUser(jobSeekerUser)).thenReturn(true);
        when(jobSeekerRepository.findByUser(jobSeekerUser)).thenReturn(Optional.of(profile));
        when(jobSeekerRepository.save(any(JobSeekerProfile.class))).thenReturn(profile);

        JobSeekerProfileRequestDto dto = new JobSeekerProfileRequestDto();
        dto.setFullName("Aditya Patil");

        JobSeekerProfileResponseDto response = jobSeekerService.createProfile(dto);
        assertNotNull(response);
        verify(jobSeekerRepository, times(1)).save(any(JobSeekerProfile.class));
    }

    @Test
    @DisplayName("Should throw JobSeekerProfileNotFoundException when profile does not exist")
    void testGetProfile_NotFound_ThrowsException() {
        mockSecurityContext("developer@gmail.com");
        when(userRepository.findByEmail("developer@gmail.com")).thenReturn(Optional.of(jobSeekerUser));
        when(jobSeekerRepository.findByUser(jobSeekerUser)).thenReturn(Optional.empty());

        assertThrows(JobSeekerProfileNotFoundException.class, () -> jobSeekerService.getProfile());
    }

    @Test
    @DisplayName("Should preserve existing resumeUrl when updating profile without a new resumeUrl")
    void testUpdateProfile_PreservesExistingResumeUrl() {
        profile.setResumeUrl("/jobseeker/resume/download/resume_user_2_abc.pdf");

        mockSecurityContext("developer@gmail.com");
        when(userRepository.findByEmail("developer@gmail.com")).thenReturn(Optional.of(jobSeekerUser));
        when(jobSeekerRepository.findByUser(jobSeekerUser)).thenReturn(Optional.of(profile));
        when(jobSeekerRepository.save(any(JobSeekerProfile.class))).thenReturn(profile);

        JobSeekerProfileRequestDto updateDto = new JobSeekerProfileRequestDto();
        updateDto.setFullName("Aditya Patil (Senior)");
        updateDto.setGender(Gender.MALE);
        updateDto.setPhoneNumber("9876543210");
        updateDto.setLocation("Bangalore");
        updateDto.setHeadline("Lead Full Stack Engineer");
        updateDto.setExperience(5);
        updateDto.setSkills("Java, Spring Boot, React, AWS");
        updateDto.setSummary("Senior full stack lead");
        updateDto.setHighestQualification(Qualification.BTECH);
        updateDto.setCollegeName("COEP Pune");
        updateDto.setResumeUrl(null); // empty update should not wipe out uploaded PDF
        updateDto.setNoticePeriod("IMMEDIATE");

        JobSeekerProfileResponseDto updated = jobSeekerService.updateProfile(updateDto);

        assertNotNull(updated);
        assertEquals("/jobseeker/resume/download/resume_user_2_abc.pdf", profile.getResumeUrl());
        assertEquals("IMMEDIATE", updated.getNoticePeriod());
    }
}
