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

        var profile = application.getJobSeekerProfile();

        dto.setApplicationId(application.getId());

        if (profile != null) {
            dto.setApplicantName(profile.getFullName());
            if (profile.getUser() != null) {
                dto.setEmail(profile.getUser().getEmail());
            }
            dto.setPhoneNumber(profile.getPhoneNumber());
            dto.setLocation(profile.getLocation());
            dto.setHeadline(profile.getHeadline());
            dto.setSummary(profile.getSummary());
            dto.setExperience(profile.getExperience());
            dto.setHighestQualification(profile.getHighestQualification());
            dto.setCollegeName(profile.getCollegeName());
            dto.setSkills(profile.getSkills());
            dto.setResumeUrl(
                    application.getAppliedResumeUrl() != null && !application.getAppliedResumeUrl().trim().isEmpty()
                            ? application.getAppliedResumeUrl()
                            : profile.getResumeUrl());
            dto.setNoticePeriod(profile.getNoticePeriod());
            dto.setGithubUrl(profile.getGithubUrl());
            dto.setLinkedinUrl(profile.getLinkedinUrl());
            dto.setPortfolioUrl(profile.getPortfolioUrl());

            if (profile.getProjects() != null) {
                dto.setProjects(profile.getProjects().stream().map(p -> {
                    com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProjectDto pdto = new com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProjectDto();
                    pdto.setId(p.getId());
                    pdto.setTitle(p.getTitle());
                    pdto.setDescription(p.getDescription());
                    pdto.setTechStack(p.getTechStack());
                    pdto.setGithubUrl(p.getGithubUrl());
                    pdto.setLiveDemoUrl(p.getLiveDemoUrl());
                    pdto.setStartDate(p.getStartDate());
                    pdto.setEndDate(p.getEndDate());
                    return pdto;
                }).toList());
            }

            if (profile.getExperiences() != null) {
                dto.setExperiences(profile.getExperiences().stream().map(e -> {
                    com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerExperienceDto edto = new com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerExperienceDto();
                    edto.setId(e.getId());
                    edto.setCompanyName(e.getCompanyName());
                    edto.setDesignation(e.getDesignation());
                    edto.setLocation(e.getLocation());
                    edto.setStartDate(e.getStartDate());
                    edto.setEndDate(e.getEndDate());
                    edto.setIsCurrentJob(e.getIsCurrentJob());
                    edto.setResponsibilities(e.getResponsibilities());
                    edto.setTechStack(e.getTechStack());
                    return edto;
                }).toList());
            }

            if (profile.getEducations() != null) {
                dto.setEducations(profile.getEducations().stream()
                        .map(com.hiresphere.hiresphere.JobSeeker.Mapper.JobSeekerEducationMapper::mapToResponseDto)
                        .toList());
            }
        }

        dto.setCoverLetter(application.getCoverLetter());
        dto.setStatus(application.getStatus());
        dto.setAppliedAt(application.getAppliedAt());

        return dto;
    }
}