package com.hiresphere.hiresphere.Job.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.hiresphere.hiresphere.Job.Dto.CreateJobRequestDto;
import com.hiresphere.hiresphere.Job.Dto.JobResponseDto;
import com.hiresphere.hiresphere.Job.Dto.PagedJobResponseDto;

public interface JobService {
	
	JobResponseDto createJob(
	        CreateJobRequestDto dto);

	List<JobResponseDto> getMyJobs();

	JobResponseDto getJobById(
	        Long jobId);

	JobResponseDto updateJob(
	        Long jobId,
	        CreateJobRequestDto dto);

	void deleteJob(
	        Long jobId);

	JobResponseDto closeJob(
	        Long jobId);

	List<JobResponseDto> getAllJobs();

	List<JobResponseDto> searchJobs(String keyword, String location, String jobType);

	List<JobResponseDto> searchJobsAdvanced(
	        String keyword,
	        String location,
	        String jobType,
	        String workplaceType,
	        Double minSalary,
	        Integer experience);

	List<JobResponseDto> searchJobsAdvanced(
	        String keyword,
	        String location,
	        String jobType,
	        String workplaceType,
	        Double minSalary,
	        Integer experience,
	        Integer postedWithinDays);

	/** Paginated + filtered job search (preferred endpoint) */
	PagedJobResponseDto searchJobsPaged(
	        String keyword,
	        String location,
	        String jobType,
	        String workplaceType,
	        Double minSalary,
	        Integer experience,
	        Pageable pageable);

	PagedJobResponseDto searchJobsPaged(
	        String keyword,
	        String location,
	        String jobType,
	        String workplaceType,
	        Double minSalary,
	        Integer experience,
	        Integer postedWithinDays,
	        Pageable pageable);
}
