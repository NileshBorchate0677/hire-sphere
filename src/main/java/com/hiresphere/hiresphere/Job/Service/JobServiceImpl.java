package com.hiresphere.hiresphere.Job.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Exception.BadRequestException;
import com.hiresphere.hiresphere.Exception.RecruiterProfileNotFoundException;
import com.hiresphere.hiresphere.Exception.ResourceNotFoundException;
import com.hiresphere.hiresphere.Exception.UnauthorizedOperationException;
import com.hiresphere.hiresphere.Job.Dto.CreateJobRequestDto;
import com.hiresphere.hiresphere.Job.Dto.JobResponseDto;
import com.hiresphere.hiresphere.Job.Dto.PagedJobResponseDto;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Job.Mapper.JobMapper;
import com.hiresphere.hiresphere.Job.Repository.JobRepository;
import com.hiresphere.hiresphere.Job.Repository.JobSpecification;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;
import com.hiresphere.hiresphere.Recruiter.Repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class JobServiceImpl implements JobService {

    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    private final UserRepository userRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobRepository jobRepository;
    private final com.hiresphere.hiresphere.Application.Repository.ApplicationRepository applicationRepository;
    private final com.hiresphere.hiresphere.SavedJob.Repository.SavedJobRepository savedJobRepository;

    // Secure helper: extract and verify authenticated recruiter profile
    private RecruiterProfile getLoggedInRecruiter() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedOperationException("Authentication required");
        }

        String email = authentication.getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (user.getRole() != UserRoles.RECRUITER) {
            throw new UnauthorizedOperationException("Only recruiter allowed to perform this operation");
        }

        return recruiterRepository.findByUser(user)
                .orElseThrow(() -> new RecruiterProfileNotFoundException("Recruiter profile not found. Please complete your recruiter profile first."));
    }

    // 1 Create Job
    @Override
    public JobResponseDto createJob(CreateJobRequestDto dto) {
        RecruiterProfile recruiter = getLoggedInRecruiter();

        if (dto.getMinSalary() != null && dto.getMaxSalary() != null && dto.getMinSalary() > dto.getMaxSalary()) {
            throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
        }

        Job job = JobMapper.mapToJob(dto);
        job.setRecruiterProfile(recruiter);

        Job savedJob = jobRepository.save(job);
        log.info("Job created successfully: id={}, title='{}', recruiter={}", savedJob.getId(), savedJob.getTitle(), recruiter.getCompanyName());

        return JobMapper.mapToJobResponseDto(savedJob);
    }

    // 2 Get My Jobs (Recruiter's own jobs)
    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDto> getMyJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedOperationException("Authentication required");
        }

        String email = authentication.getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != UserRoles.RECRUITER) {
            throw new UnauthorizedOperationException("Only recruiter allowed");
        }

        var recruiterOpt = recruiterRepository.findByUser(user);
        if (recruiterOpt.isEmpty()) {
            return List.of();
        }

        return jobRepository.findByRecruiterProfile(recruiterOpt.get())
                .stream()
                .map(JobMapper::mapToJobResponseDto)
                .toList();
    }

    // 3 Get Job By Id
    @Override
    @Transactional(readOnly = true)
    public JobResponseDto getJobById(Long jobId) {
        Job job = jobRepository.findByIdWithRecruiter(jobId)
                .orElseGet(() -> jobRepository.findById(jobId)
                        .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId)));

        return JobMapper.mapToJobResponseDto(job);
    }

    // 4 Update Job
    @Override
    public JobResponseDto updateJob(Long jobId, CreateJobRequestDto dto) {
        RecruiterProfile recruiter = getLoggedInRecruiter();

        Job job = jobRepository.findByIdWithRecruiter(jobId)
                .orElseGet(() -> jobRepository.findById(jobId)
                        .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId)));

        if (!job.getRecruiterProfile().getRecruiter_Profile_id().equals(recruiter.getRecruiter_Profile_id())) {
            log.warn("Security violation: Recruiter ID {} attempted to update Job ID {} owned by Recruiter ID {}",
                    recruiter.getRecruiter_Profile_id(), jobId, job.getRecruiterProfile().getRecruiter_Profile_id());
            throw new UnauthorizedOperationException("You are not authorized to update this job");
        }

        if (dto.getMinSalary() != null && dto.getMaxSalary() != null && dto.getMinSalary() > dto.getMaxSalary()) {
            throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
        }

        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setLocation(dto.getLocation());
        job.setMinSalary(dto.getMinSalary());
        job.setMaxSalary(dto.getMaxSalary());
        job.setExperienceRequired(dto.getExperienceRequired());
        job.setVacancies(dto.getVacancies());
        job.setRequiredSkills(dto.getRequiredSkills());
        job.setJobType(dto.getJobType());
        if (dto.getWorkplaceType() != null) {
            job.setWorkplaceType(dto.getWorkplaceType());
        }
        job.setApplicationDeadline(dto.getApplicationDeadline());
        if (dto.getStatus() != null) {
            job.setStatus(dto.getStatus());
        }

        Job updatedJob = jobRepository.save(job);
        log.info("Job updated successfully: id={}", updatedJob.getId());

        return JobMapper.mapToJobResponseDto(updatedJob);
    }

    // 5 Delete Job
    @Override
    public void deleteJob(Long jobId) {
        RecruiterProfile recruiter = getLoggedInRecruiter();

        Job job = jobRepository.findByIdWithRecruiter(jobId)
                .orElseGet(() -> jobRepository.findById(jobId)
                        .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId)));

        if (!job.getRecruiterProfile().getRecruiter_Profile_id().equals(recruiter.getRecruiter_Profile_id())) {
            log.warn("Security violation: Recruiter ID {} attempted to delete Job ID {} owned by Recruiter ID {}",
                    recruiter.getRecruiter_Profile_id(), jobId, job.getRecruiterProfile().getRecruiter_Profile_id());
            throw new UnauthorizedOperationException("You are not authorized to delete this job");
        }

        // Remove applications and saved bookmarks for this job first to preserve referential integrity
        savedJobRepository.deleteByJobIn(List.of(job));
        applicationRepository.deleteByJobIn(List.of(job));
        jobRepository.delete(job);
        log.info("Job and associated applications deleted successfully: id={}", jobId);
    }

    // 6 Close Job
    @Override
    public JobResponseDto closeJob(Long jobId) {
        RecruiterProfile recruiter = getLoggedInRecruiter();

        Job job = jobRepository.findByIdWithRecruiter(jobId)
                .orElseGet(() -> jobRepository.findById(jobId)
                        .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId)));

        if (!job.getRecruiterProfile().getRecruiter_Profile_id().equals(recruiter.getRecruiter_Profile_id())) {
            log.warn("Security violation: Recruiter ID {} attempted to close Job ID {} owned by Recruiter ID {}",
                    recruiter.getRecruiter_Profile_id(), jobId, job.getRecruiterProfile().getRecruiter_Profile_id());
            throw new UnauthorizedOperationException("You are not authorized to close this job");
        }

        job.setStatus(JobStatus.CLOSED);
        Job updatedJob = jobRepository.save(job);
        log.info("Job closed successfully: id={}", jobId);

        return JobMapper.mapToJobResponseDto(updatedJob);
    }

    // 7 Get All Open Jobs
    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDto> getAllJobs() {
        return jobRepository.findByStatus(JobStatus.OPEN)
                .stream()
                .map(JobMapper::mapToJobResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDto> searchJobs(String keyword, String location, String jobType) {
        return searchJobsAdvanced(keyword, location, jobType, null, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDto> searchJobsAdvanced(
            String keyword,
            String location,
            String jobType,
            String workplaceType,
            Double minSalary,
            Integer experience) {
        return searchJobsAdvanced(keyword, location, jobType, workplaceType, minSalary, experience, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobResponseDto> searchJobsAdvanced(
            String keyword,
            String location,
            String jobType,
            String workplaceType,
            Double minSalary,
            Integer experience,
            Integer postedWithinDays) {

        var spec = JobSpecification.filterJobs(
                keyword, location, jobType, workplaceType, minSalary, experience, postedWithinDays);

        return jobRepository.findAll(spec)
                .stream()
                .map(JobMapper::mapToJobResponseDto)
                .toList();
    }

    // 9 Paginated Search (production default)
    @Override
    @Transactional(readOnly = true)
    public PagedJobResponseDto searchJobsPaged(
            String keyword,
            String location,
            String jobType,
            String workplaceType,
            Double minSalary,
            Integer experience,
            Pageable pageable) {
        return searchJobsPaged(keyword, location, jobType, workplaceType, minSalary, experience, null, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedJobResponseDto searchJobsPaged(
            String keyword,
            String location,
            String jobType,
            String workplaceType,
            Double minSalary,
            Integer experience,
            Integer postedWithinDays,
            Pageable pageable) {

        var spec = JobSpecification.filterJobs(
                keyword, location, jobType, workplaceType, minSalary, experience, postedWithinDays);

        Page<Job> page = jobRepository.findAll(spec, pageable);

        List<JobResponseDto> content = page.getContent()
                .stream()
                .map(JobMapper::mapToJobResponseDto)
                .toList();

        return PagedJobResponseDto.builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}