package com.hiresphere.hiresphere.Job;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Exception.BadRequestException;
import com.hiresphere.hiresphere.Exception.UnauthorizedOperationException;
import com.hiresphere.hiresphere.Job.Dto.CreateJobRequestDto;
import com.hiresphere.hiresphere.Job.Dto.JobResponseDto;
import com.hiresphere.hiresphere.Job.Dto.PagedJobResponseDto;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Job.Enums.JobType;
import com.hiresphere.hiresphere.Job.Repository.JobRepository;
import com.hiresphere.hiresphere.Job.Service.JobServiceImpl;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;
import com.hiresphere.hiresphere.Recruiter.Repository.RecruiterRepository;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private com.hiresphere.hiresphere.Application.Repository.ApplicationRepository applicationRepository;

    @InjectMocks
    private JobServiceImpl jobService;

    private Users recruiterUser;
    private RecruiterProfile recruiterProfile;
    private Job job;

    @BeforeEach
    void setUp() {
        recruiterUser = new Users();
        recruiterUser.setUserId(1L);
        recruiterUser.setEmail("recruiter@techcorp.in");
        recruiterUser.setRole(UserRoles.RECRUITER);

        recruiterProfile = new RecruiterProfile();
        recruiterProfile.setRecruiter_Profile_id(10L);
        recruiterProfile.setCompanyName("TechCorp India");
        recruiterProfile.setUser(recruiterUser);

        job = new Job();
        job.setId(100L);
        job.setTitle("Senior Java Developer");
        job.setDescription("5+ years experience in Spring Boot, Microservices and AWS");
        job.setLocation("Pune");
        job.setMinSalary(12.0);
        job.setMaxSalary(18.0);
        job.setExperienceRequired(5);
        job.setVacancies(3);
        job.setRequiredSkills("Java, Spring Boot, MySQL, Docker");
        job.setJobType(JobType.FULL_TIME);
        job.setStatus(JobStatus.OPEN);
        job.setApplicationDeadline(LocalDate.now().plusMonths(1));
        job.setRecruiterProfile(recruiterProfile);
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
    @DisplayName("Should create job successfully when salary is valid")
    void testCreateJob_Success() {
        mockSecurityContext("recruiter@techcorp.in");
        when(userRepository.findByEmail("recruiter@techcorp.in")).thenReturn(Optional.of(recruiterUser));
        when(recruiterRepository.findByUser(recruiterUser)).thenReturn(Optional.of(recruiterProfile));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        CreateJobRequestDto dto = new CreateJobRequestDto();
        dto.setTitle("Senior Java Developer");
        dto.setDescription("5+ years experience in Spring Boot, Microservices and AWS");
        dto.setLocation("Pune");
        dto.setMinSalary(12.0);
        dto.setMaxSalary(18.0);
        dto.setExperienceRequired(5);
        dto.setVacancies(3);
        dto.setRequiredSkills("Java, Spring Boot, MySQL, Docker");
        dto.setJobType(JobType.FULL_TIME);
        dto.setApplicationDeadline(LocalDate.now().plusMonths(1));

        JobResponseDto response = jobService.createJob(dto);

        assertNotNull(response);
        assertEquals("Senior Java Developer", response.getTitle());
        assertEquals("TechCorp India", response.getCompanyName());
        assertEquals(JobStatus.OPEN, response.getStatus());
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    @DisplayName("Should throw BadRequestException when minSalary > maxSalary")
    void testCreateJob_InvalidSalary_ThrowsBadRequestException() {
        mockSecurityContext("recruiter@techcorp.in");
        when(userRepository.findByEmail("recruiter@techcorp.in")).thenReturn(Optional.of(recruiterUser));
        when(recruiterRepository.findByUser(recruiterUser)).thenReturn(Optional.of(recruiterProfile));

        CreateJobRequestDto dto = new CreateJobRequestDto();
        dto.setMinSalary(25.0);
        dto.setMaxSalary(15.0); // min > max

        BadRequestException ex = assertThrows(BadRequestException.class, () -> jobService.createJob(dto));
        assertTrue(ex.getMessage().contains("Minimum salary cannot be greater than maximum salary"));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    @DisplayName("Should throw UnauthorizedOperationException when non-owner recruiter attempts to update job")
    void testUpdateJob_UnauthorizedOwner_ThrowsException() {
        mockSecurityContext("recruiter@techcorp.in");
        when(userRepository.findByEmail("recruiter@techcorp.in")).thenReturn(Optional.of(recruiterUser));
        when(recruiterRepository.findByUser(recruiterUser)).thenReturn(Optional.of(recruiterProfile));

        RecruiterProfile otherRecruiter = new RecruiterProfile();
        otherRecruiter.setRecruiter_Profile_id(999L);
        otherRecruiter.setCompanyName("Other Corp");

        Job otherJob = new Job();
        otherJob.setId(200L);
        otherJob.setRecruiterProfile(otherRecruiter);

        when(jobRepository.findByIdWithRecruiter(200L)).thenReturn(Optional.of(otherJob));

        CreateJobRequestDto dto = new CreateJobRequestDto();
        dto.setTitle("Updated Title");

        assertThrows(UnauthorizedOperationException.class, () -> jobService.updateJob(200L, dto));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    @DisplayName("Should throw UnauthorizedOperationException when non-owner recruiter attempts to delete job")
    void testDeleteJob_UnauthorizedOwner_ThrowsException() {
        mockSecurityContext("recruiter@techcorp.in");
        when(userRepository.findByEmail("recruiter@techcorp.in")).thenReturn(Optional.of(recruiterUser));
        when(recruiterRepository.findByUser(recruiterUser)).thenReturn(Optional.of(recruiterProfile));

        RecruiterProfile otherRecruiter = new RecruiterProfile();
        otherRecruiter.setRecruiter_Profile_id(999L);

        Job otherJob = new Job();
        otherJob.setId(200L);
        otherJob.setRecruiterProfile(otherRecruiter);

        when(jobRepository.findByIdWithRecruiter(200L)).thenReturn(Optional.of(otherJob));

        assertThrows(UnauthorizedOperationException.class, () -> jobService.deleteJob(200L));
        verify(jobRepository, never()).delete(any(Job.class));
    }

    @Test
    @DisplayName("Should return only OPEN jobs in getAllJobs")
    void testGetAllJobs_ReturnsOpenJobs() {
        when(jobRepository.findByStatus(JobStatus.OPEN)).thenReturn(List.of(job));

        List<JobResponseDto> result = jobService.getAllJobs();

        assertEquals(1, result.size());
        assertEquals("Senior Java Developer", result.get(0).getTitle());
    }

    @Test
    @DisplayName("Should filter jobs by keyword correctly in searchJobs")
    void testSearchJobs_KeywordMatch() {
        when(jobRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class)))
                .thenReturn(List.of(job));

        List<JobResponseDto> result = jobService.searchJobs("Spring Boot", "Pune", null);

        assertEquals(1, result.size());
        assertEquals("Senior Java Developer", result.get(0).getTitle());
    }

    @Test
    @DisplayName("Should return empty list when keyword doesn't match in searchJobs")
    void testSearchJobs_NoMatch() {
        when(jobRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class)))
                .thenReturn(List.of());

        List<JobResponseDto> result = jobService.searchJobs("Python Django", "Pune", null);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should paginate job search results correctly")
    void testSearchJobsPaged_ReturnsPagedResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        when(jobRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(job), pageable, 1));

        PagedJobResponseDto response = jobService.searchJobsPaged("Java", "Pune", null, null, null, null, null, pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(0, response.getPage());
        assertEquals(10, response.getSize());
        assertEquals(1, response.getTotalElements());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
    }
}
