package com.hiresphere.hiresphere.Job.Mapper;

import com.hiresphere.hiresphere.Job.Dto.CreateJobRequestDto;
import com.hiresphere.hiresphere.Job.Dto.JobResponseDto;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Job.Enums.WorkplaceType;

public class JobMapper {

    // Map Dto To Entity
    public static Job mapToJob(CreateJobRequestDto dto) {
        Job job = new Job();

        job.setTitle(dto.getTitle() != null ? dto.getTitle().trim() : null);
        job.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        job.setLocation(dto.getLocation() != null ? dto.getLocation().trim() : null);

        job.setMinSalary(dto.getMinSalary());
        job.setMaxSalary(dto.getMaxSalary());

        job.setExperienceRequired(dto.getExperienceRequired());
        job.setVacancies(dto.getVacancies());
        job.setRequiredSkills(dto.getRequiredSkills() != null ? dto.getRequiredSkills().trim() : null);
        job.setJobType(dto.getJobType());

        job.setWorkplaceType(
                dto.getWorkplaceType() != null
                        ? dto.getWorkplaceType()
                        : WorkplaceType.ON_SITE
        );

        job.setApplicationDeadline(dto.getApplicationDeadline());

        // Status: use DTO value if provided, default to OPEN
        job.setStatus(dto.getStatus() != null ? dto.getStatus() : JobStatus.OPEN);

        return job;
    }

    // Entity -> Response DTO
    public static JobResponseDto mapToJobResponseDto(Job job) {
        if (job == null) return null;

        JobResponseDto dto = new JobResponseDto();

        dto.setId(job.getId());

        // Recruiter Profile
        if (job.getRecruiterProfile() != null) {
            dto.setRecruiterId(job.getRecruiterProfile().getRecruiter_Profile_id());
            dto.setCompanyName(
                    job.getRecruiterProfile().getCompanyName() != null && !job.getRecruiterProfile().getCompanyName().isBlank()
                            ? job.getRecruiterProfile().getCompanyName()
                            : "HireSphere Partner"
            );
        } else {
            dto.setCompanyName("HireSphere Partner");
        }

        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setLocation(job.getLocation());
        dto.setMinSalary(job.getMinSalary());
        dto.setMaxSalary(job.getMaxSalary());
        dto.setExperienceRequired(job.getExperienceRequired());
        dto.setVacancies(job.getVacancies());
        dto.setRequiredSkills(job.getRequiredSkills());
        dto.setJobType(job.getJobType());
        dto.setWorkplaceType(job.getWorkplaceType());
        dto.setStatus(job.getStatus());
        dto.setApplicationDeadline(job.getApplicationDeadline());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setUpdatedAt(job.getUpdatedAt());

        return dto;
    }
}