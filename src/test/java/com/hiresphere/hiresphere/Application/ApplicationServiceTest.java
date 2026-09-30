package com.hiresphere.hiresphere.Application;

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

import com.hiresphere.hiresphere.Application.DTO.ApplicantResponseDto;
import com.hiresphere.hiresphere.Application.DTO.ApplyJobRequestDto;
import com.hiresphere.hiresphere.Application.DTO.MyApplicationResponseDto;
import com.hiresphere.hiresphere.Application.Entity.Application;
import com.hiresphere.hiresphere.Application.Enums.ApplicationStatus;
import com.hiresphere.hiresphere.Application.Repository.ApplicationRepository;
import com.hiresphere.hiresphere.Application.Service.ApplicationServiceImpl;
import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Exception.BadRequestException;
import com.hiresphere.hiresphere.Exception.DuplicateResourceException;
import com.hiresphere.hiresphere.Exception.UnauthorizedOperationException;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Job.Repository.JobRepository;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerRepository;
import com.hiresphere.hiresphere.Notification.Service.NotificationService;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;
import com.hiresphere.hiresphere.Recruiter.Repository.RecruiterRepository;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobSeekerRepository jobSeekerRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private Users seekerUser;
    private JobSeekerProfile seekerProfile;
    private Users recruiterUser;
    private RecruiterProfile recruiterProfile;
    private Job openJob;
    private Application application;

    @BeforeEach
    void setUp() {
        seekerUser = new Users();
        seekerUser.setUserId(2L);
        seekerUser.setEmail("developer@gmail.com");
        seekerUser.setRole(UserRoles.JOB_SEEKER);

        seekerProfile = new JobSeekerProfile();
        seekerProfile.setJobSeekerProfileId(20L);
        seekerProfile.setFullName("Aditya Patil");
        seekerProfile.setResumeUrl("/jobseeker/resume/download/resume_user_2_abc.pdf");
        seekerProfile.setUser(seekerUser);

        recruiterUser = new Users();
        recruiterUser.setUserId(1L);
        recruiterUser.setEmail("recruiter@techcorp.in");
        recruiterUser.setRole(UserRoles.RECRUITER);

        recruiterProfile = new RecruiterProfile();
        recruiterProfile.setRecruiter_Profile_id(10L);
        recruiterProfile.setCompanyName("TechCorp India");
        recruiterProfile.setUser(recruiterUser);

        openJob = new Job();
        openJob.setId(100L);
        openJob.setTitle("Java Developer");
        openJob.setStatus(JobStatus.OPEN);
        openJob.setRecruiterProfile(recruiterProfile);

        application = new Application();
        application.setId(500L);
        application.setJob(openJob);
        application.setJobSeekerProfile(seekerProfile);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setCoverLetter("Excited to apply!");
    }

    private void mockSecurityContext(String email) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);
        when(auth.isAuthenticated()).thenReturn(true);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Job Seeker should apply to an open job successfully")
    void testApplyJob_Success() {
        mockSecurityContext("developer@gmail.com");
        when(userRepository.findByEmail("developer@gmail.com")).thenReturn(Optional.of(seekerUser));
        when(jobSeekerRepository.findByUser(seekerUser)).thenReturn(Optional.of(seekerProfile));
        when(jobRepository.findById(100L)).thenReturn(Optional.of(openJob));
        when(applicationRepository.existsByJobAndJobSeekerProfile(openJob, seekerProfile)).thenReturn(false);
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        ApplyJobRequestDto dto = new ApplyJobRequestDto();
        dto.setCoverLetter("Excited to apply!");

        MyApplicationResponseDto response = applicationService.applyJob(100L, dto);

        assertNotNull(response);
        assertEquals(100L, response.getJobId());
        assertEquals("Java Developer", response.getJobTitle());
        assertEquals("TechCorp India", response.getCompanyName());
        assertEquals(ApplicationStatus.APPLIED, response.getStatus());
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    @DisplayName("Should prevent double application for the same job with DuplicateResourceException")
    void testApplyJob_AlreadyApplied_ThrowsDuplicateResourceException() {
        mockSecurityContext("developer@gmail.com");
        when(userRepository.findByEmail("developer@gmail.com")).thenReturn(Optional.of(seekerUser));
        when(jobSeekerRepository.findByUser(seekerUser)).thenReturn(Optional.of(seekerProfile));
        when(jobRepository.findById(100L)).thenReturn(Optional.of(openJob));
        when(applicationRepository.existsByJobAndJobSeekerProfile(openJob, seekerProfile)).thenReturn(true);

        ApplyJobRequestDto dto = new ApplyJobRequestDto();

        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class, () -> applicationService.applyJob(100L, dto));
        assertTrue(ex.getMessage().contains("You have already applied"));
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    @DisplayName("Should prevent applying to closed jobs with BadRequestException")
    void testApplyJob_ClosedJob_ThrowsBadRequestException() {
        openJob.setStatus(JobStatus.CLOSED);

        mockSecurityContext("developer@gmail.com");
        when(userRepository.findByEmail("developer@gmail.com")).thenReturn(Optional.of(seekerUser));
        when(jobSeekerRepository.findByUser(seekerUser)).thenReturn(Optional.of(seekerProfile));
        when(jobRepository.findById(100L)).thenReturn(Optional.of(openJob));

        ApplyJobRequestDto dto = new ApplyJobRequestDto();

        BadRequestException ex = assertThrows(BadRequestException.class, () -> applicationService.applyJob(100L, dto));
        assertTrue(ex.getMessage().contains("Job is not accepting applications"));
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    @DisplayName("Recruiter should be able to shortlist applicant and view candidate's resume")
    void testShortlistApplication_Success() {
        mockSecurityContext("recruiter@techcorp.in");
        when(userRepository.findByEmail("recruiter@techcorp.in")).thenReturn(Optional.of(recruiterUser));
        when(recruiterRepository.findByUser(recruiterUser)).thenReturn(Optional.of(recruiterProfile));
        when(applicationRepository.findByIdWithDetails(500L)).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        ApplicantResponseDto result = applicationService.shortlistApplication(500L);

        assertNotNull(result);
        assertEquals(ApplicationStatus.SHORTLISTED, application.getStatus());
        assertEquals("Aditya Patil", result.getApplicantName());
        assertEquals("/jobseeker/resume/download/resume_user_2_abc.pdf", result.getResumeUrl());
        verify(applicationRepository, times(1)).save(application);
    }

    @Test
    @DisplayName("Recruiter cannot modify application belonging to another recruiter's job")
    void testShortlistApplication_UnauthorizedRecruiter_ThrowsException() {
        RecruiterProfile otherRecruiter = new RecruiterProfile();
        otherRecruiter.setRecruiter_Profile_id(99L);

        mockSecurityContext("recruiter@techcorp.in");
        when(userRepository.findByEmail("recruiter@techcorp.in")).thenReturn(Optional.of(recruiterUser));
        when(recruiterRepository.findByUser(recruiterUser)).thenReturn(Optional.of(otherRecruiter));
        when(applicationRepository.findByIdWithDetails(500L)).thenReturn(Optional.of(application));

        UnauthorizedOperationException ex = assertThrows(UnauthorizedOperationException.class, () -> applicationService.shortlistApplication(500L));
        assertTrue(ex.getMessage().contains("You can update only your own applicants"));
    }

    @Test
    @DisplayName("Should prevent candidate from withdrawing an accepted application")
    void testWithdrawApplication_AlreadyAccepted_ThrowsBadRequestException() {
        mockSecurityContext("developer@gmail.com");
        when(userRepository.findByEmail("developer@gmail.com")).thenReturn(Optional.of(seekerUser));
        when(jobSeekerRepository.findByUser(seekerUser)).thenReturn(Optional.of(seekerProfile));

        application.setStatus(ApplicationStatus.ACCEPTED);
        when(applicationRepository.findByIdAndJobSeekerProfile(500L, seekerProfile)).thenReturn(Optional.of(application));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> applicationService.withdrawApplication(500L));
        assertTrue(ex.getMessage().contains("Accepted application cannot be withdrawn"));
    }
}
