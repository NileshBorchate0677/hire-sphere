package com.hiresphere.hiresphere.JobSeeker.Mapper;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;

public class JobSeekerMapper {

    // DTO -> Entity

    public static JobSeekerProfile mapToJobSeekerProfile(
            JobSeekerProfileRequestDto dto)
    {
        JobSeekerProfile profile =
                new JobSeekerProfile();

        profile.setFullName(dto.getFullName());
        profile.setGender(dto.getGender());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setLocation(dto.getLocation());
        profile.setHeadline(dto.getHeadline());
        profile.setExperience(dto.getExperience()); 
        profile.setSkills(dto.getSkills());
        profile.setSummary(dto.getSummary());
        profile.setHighestQualification(
                dto.getHighestQualification());
        profile.setCollegeName(dto.getCollegeName());
        profile.setCourse(dto.getCourse());
        profile.setPassingYear(dto.getPassingYear());
        profile.setCurrentDesignation(dto.getCurrentDesignation());
        profile.setCurrentCompany(dto.getCurrentCompany());
        profile.setCurrentSalary(dto.getCurrentSalary());
        profile.setExpectedSalary(dto.getExpectedSalary());
        profile.setPreferredLocation(dto.getPreferredLocation());
        profile.setResumeUrl(dto.getResumeUrl() != null ? dto.getResumeUrl() : "");
        profile.setResumeFileName(dto.getResumeFileName());
        profile.setNoticePeriod(dto.getNoticePeriod());
        profile.setGithubUrl(dto.getGithubUrl());
        profile.setLinkedinUrl(dto.getLinkedinUrl());
        profile.setPortfolioUrl(dto.getPortfolioUrl());

        // Naukri Career Profile
        profile.setCurrentIndustry(dto.getCurrentIndustry());
        profile.setDepartment(dto.getDepartment());
        profile.setRoleCategory(dto.getRoleCategory());
        profile.setDesiredJobType(dto.getDesiredJobType());
        profile.setDesiredEmploymentType(dto.getDesiredEmploymentType());
        profile.setPreferredWorkMode(dto.getPreferredWorkMode());
        profile.setPreferredShift(dto.getPreferredShift());

        // Naukri Personal Details
        profile.setDateOfBirth(dto.getDateOfBirth());
        profile.setMaritalStatus(dto.getMaritalStatus());
        profile.setHometown(dto.getHometown());
        profile.setPincode(dto.getPincode());
        profile.setPermanentAddress(dto.getPermanentAddress());
        profile.setLanguagesKnown(dto.getLanguagesKnown());
        profile.setDifferentlyAbled(dto.getDifferentlyAbled());
        profile.setCareerBreak(dto.getCareerBreak());

        return profile;
    }

    // Entity -> Response DTO

    public static JobSeekerProfileResponseDto
    mapToJobSeekerProfileResponseDto(
            JobSeekerProfile profile)
    {
        JobSeekerProfileResponseDto dto =
                new JobSeekerProfileResponseDto();

        dto.setJobSeekerProfileId(
                profile.getJobSeekerProfileId());

        dto.setFullName(profile.getFullName());
        if (profile.getUser() != null) {
            try {
                dto.setEmail(profile.getUser().getEmail());
            } catch (Exception ignored) {
            }
        }
        dto.setGender(profile.getGender());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setLocation(profile.getLocation());
        dto.setHeadline(profile.getHeadline());
        dto.setExperience(profile.getExperience());
        dto.setSkills(profile.getSkills());
        dto.setSummary(profile.getSummary());
        dto.setHighestQualification(
                profile.getHighestQualification());
        dto.setCollegeName(profile.getCollegeName());
        dto.setCourse(profile.getCourse());
        dto.setPassingYear(profile.getPassingYear());
        dto.setCurrentDesignation(profile.getCurrentDesignation());
        dto.setCurrentCompany(profile.getCurrentCompany());
        dto.setCurrentSalary(profile.getCurrentSalary());
        dto.setExpectedSalary(profile.getExpectedSalary());
        dto.setPreferredLocation(profile.getPreferredLocation());
        dto.setResumeUrl(profile.getResumeUrl());
        dto.setResumeFileName(profile.getResumeFileName());
        dto.setNoticePeriod(profile.getNoticePeriod());
        dto.setGithubUrl(profile.getGithubUrl());
        dto.setLinkedinUrl(profile.getLinkedinUrl());
        dto.setPortfolioUrl(profile.getPortfolioUrl());

        // Naukri Career Profile
        dto.setCurrentIndustry(profile.getCurrentIndustry());
        dto.setDepartment(profile.getDepartment());
        dto.setRoleCategory(profile.getRoleCategory());
        dto.setDesiredJobType(profile.getDesiredJobType());
        dto.setDesiredEmploymentType(profile.getDesiredEmploymentType());
        dto.setPreferredWorkMode(profile.getPreferredWorkMode());
        dto.setPreferredShift(profile.getPreferredShift());

        // Naukri Personal Details
        dto.setDateOfBirth(profile.getDateOfBirth());
        dto.setMaritalStatus(profile.getMaritalStatus());
        dto.setHometown(profile.getHometown());
        dto.setPincode(profile.getPincode());
        dto.setPermanentAddress(profile.getPermanentAddress());
        dto.setLanguagesKnown(profile.getLanguagesKnown());
        dto.setDifferentlyAbled(profile.getDifferentlyAbled());
        dto.setCareerBreak(profile.getCareerBreak());

        try {
            if (profile.getEducations() != null) {
                dto.setEducations(profile.getEducations().stream()
                        .map(JobSeekerEducationMapper::mapToResponseDto)
                        .toList());
            }
        } catch (Exception ignored) {
        }

        try {
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
        } catch (Exception ignored) {
        }

        try {
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
        } catch (Exception ignored) {
        }

        dto.setCreatedAt(profile.getCreatedAt());

        return dto;
    }
}