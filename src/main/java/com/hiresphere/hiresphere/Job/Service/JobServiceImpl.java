package com.hiresphere.hiresphere.Job.Service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Job.Dto.CreateJobRequestDto;
import com.hiresphere.hiresphere.Job.Dto.JobResponseDto;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Job.Mapper.JobMapper;
import com.hiresphere.hiresphere.Job.Repository.JobRepository;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;
import com.hiresphere.hiresphere.Recruiter.Repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final UserRepository userRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobRepository jobRepository;

    // Common Method
    private RecruiterProfile getLoggedInRecruiter()
    {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Users user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        if(user.getRole() != UserRoles.RECRUITER)
        {
            throw new RuntimeException(
                    "Only recruiter allowed");
        }

        return recruiterRepository
                .findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Recruiter profile not found"));
    }

    
    
    
    
    
    
    // 1 Create Job

    @Override
    public JobResponseDto createJob(
            CreateJobRequestDto dto)
    {
        RecruiterProfile recruiter =
                getLoggedInRecruiter();

        if(dto.getMinSalary() >
                dto.getMaxSalary())
        {
            throw new RuntimeException(
                    "Minimum salary cannot be greater than maximum salary");
        }

        Job job =
                JobMapper.mapToJob(dto);

        job.setRecruiterProfile(recruiter);

        Job savedJob =
                jobRepository.save(job);

        return JobMapper
                .mapToJobResponseDto(savedJob);
    }

    
    
    
    
    
    // 2 Get My Jobs

    @Override
    public List<JobResponseDto> getMyJobs()
    {
        RecruiterProfile recruiter =
                getLoggedInRecruiter();

        return jobRepository
                .findByRecruiterProfile(recruiter)
                .stream() 
                .map(JobMapper::mapToJobResponseDto)
                .toList();
    }

    // 3 Get Job By Id 

    @Override
    public JobResponseDto getJobById(
            Long jobId)
    {
        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        return JobMapper
                .mapToJobResponseDto(job);
    }

    
    
    
    
    // 4 Update Job

    @Override
    public JobResponseDto updateJob(
            Long jobId,
            CreateJobRequestDto dto)
    {
        RecruiterProfile recruiter =
                getLoggedInRecruiter();

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        if(!job.getRecruiterProfile()
                .getRecruiter_Profile_id()
                .equals(
                        recruiter.getRecruiter_Profile_id()))
        {
            throw new RuntimeException(
                    "Unauthorized");
        }

        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setLocation(dto.getLocation());
        job.setMinSalary(dto.getMinSalary());
        job.setMaxSalary(dto.getMaxSalary());
        job.setExperienceRequired(
                dto.getExperienceRequired());
        job.setVacancies(
                dto.getVacancies());
        job.setRequiredSkills(
                dto.getRequiredSkills());
        job.setJobType(
                dto.getJobType());
        job.setApplicationDeadline(
                dto.getApplicationDeadline());

        Job updatedJob =
                jobRepository.save(job);

        return JobMapper
                .mapToJobResponseDto(updatedJob);
    }
    
    
    
    

    // 5 Delete Job

    @Override
    public void deleteJob(Long jobId)
    {
        RecruiterProfile recruiter =
                getLoggedInRecruiter();

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        if(!job.getRecruiterProfile()
                .getRecruiter_Profile_id()
                .equals(
                        recruiter.getRecruiter_Profile_id()))
        {
            throw new RuntimeException(
                    "Unauthorized");
        }

        jobRepository.delete(job);
    }
    
    
    

    // 6 Close Job

    @Override
    public JobResponseDto closeJob(
            Long jobId)
    {
        RecruiterProfile recruiter =
                getLoggedInRecruiter();

        Job job =
                jobRepository.findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        if(!job.getRecruiterProfile()
                .getRecruiter_Profile_id()
                .equals(
                        recruiter.getRecruiter_Profile_id()))
        {
            throw new RuntimeException(
                    "Unauthorized");
        }

        job.setStatus(JobStatus.CLOSED);

        Job updatedJob =
                jobRepository.save(job);

        return JobMapper
                .mapToJobResponseDto(updatedJob);
    }

    
    
    
    // 7 Get All Jobs

    @Override
    public List<JobResponseDto> getAllJobs()
    {
        return jobRepository
                .findByStatus(JobStatus.OPEN)
                .stream()
                .map(JobMapper::mapToJobResponseDto)
                .toList();
    }
}