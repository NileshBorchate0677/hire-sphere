package com.hiresphere.hiresphere.Job.Service;

import java.util.List;

import com.hiresphere.hiresphere.Job.Dto.CreateJobRequestDto;
import com.hiresphere.hiresphere.Job.Dto.JobResponseDto;

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

}
