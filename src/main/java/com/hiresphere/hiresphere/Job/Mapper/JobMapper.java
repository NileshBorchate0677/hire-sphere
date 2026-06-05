package com.hiresphere.hiresphere.Job.Mapper;

import com.hiresphere.hiresphere.Job.Dto.CreateJobRequestDto;
import com.hiresphere.hiresphere.Job.Dto.JobResponseDto;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;

public class JobMapper {

    // Map Dto To Entity
    public static Job mapToJob(CreateJobRequestDto dto){
        Job job = new Job();

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

        // New Job Default Status
        job.setStatus(JobStatus.OPEN);

        return job;
    }


    // Entity -> Response DTO
    public static JobResponseDto mapToJobResponseDto( Job job){
        JobResponseDto dto =  new JobResponseDto();

        dto.setId(job.getId());
        
        // Recruiter Profile
        dto.setCompanyName(
                job.getRecruiterProfile()
                   .getCompanyName());


        dto.setTitle(job.getTitle());

        dto.setDescription(
                job.getDescription());

        dto.setLocation(
                job.getLocation());

        dto.setMinSalary(
                job.getMinSalary());

        dto.setMaxSalary(
                job.getMaxSalary());

        dto.setExperienceRequired(
                job.getExperienceRequired());

        dto.setVacancies(
                job.getVacancies());

        dto.setRequiredSkills(
                job.getRequiredSkills());

        dto.setJobType(
                job.getJobType());

        dto.setStatus(
                job.getStatus());

        dto.setApplicationDeadline(
                job.getApplicationDeadline());

        dto.setCreatedAt(
                job.getCreatedAt());

       
        return dto;
    }

}