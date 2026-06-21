package com.hiresphere.hiresphere.Application.Mapper;

import com.hiresphere.hiresphere.Application.DTO.ApplicantResponseDto;
import com.hiresphere.hiresphere.Application.DTO.MyApplicationResponseDto;
import com.hiresphere.hiresphere.Application.Entity.Application;

public class ApplicationMapper {

	
	
    // Job Seeker View

    public static MyApplicationResponseDto
    mapToMyApplicationResponseDto(
            Application application)
    {
        MyApplicationResponseDto dto =
                new MyApplicationResponseDto();

        dto.setApplicationId(
                application.getId());

        dto.setJobId(
                application.getJob().getId());

        dto.setJobTitle(
                application.getJob().getTitle());

        dto.setCompanyName(
                application.getJob()
                        .getRecruiterProfile()
                        .getCompanyName());

        dto.setStatus(
                application.getStatus());

        dto.setAppliedAt(
                application.getAppliedAt());

        return dto;
    }

    
    
    
    // Recruiter View

    public static ApplicantResponseDto
    mapToApplicantResponseDto(
            Application application)
    {
        ApplicantResponseDto dto =
                new ApplicantResponseDto();

        dto.setApplicationId(
                application.getId());

        dto.setApplicantName(
                application.getJobSeekerProfile()
                        .getFullName());

        dto.setPhoneNumber(
                application.getJobSeekerProfile()
                        .getPhoneNumber());

        dto.setLocation(
                application.getJobSeekerProfile()
                        .getLocation());

        dto.setExperience(
                application.getJobSeekerProfile()
                        .getExperience());

        dto.setHighestQualification(
                application.getJobSeekerProfile()
                        .getHighestQualification());

        dto.setSkills(
                application.getJobSeekerProfile()
                        .getSkills());

        dto.setResumeUrl(
                application.getJobSeekerProfile()
                        .getResumeUrl());

        dto.setStatus(
                application.getStatus());

        dto.setAppliedAt(
                application.getAppliedAt());

        return dto;
    }
}