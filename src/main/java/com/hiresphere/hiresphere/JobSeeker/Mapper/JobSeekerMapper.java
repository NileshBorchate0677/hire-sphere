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
        profile.setResumeUrl(dto.getResumeUrl());

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
        dto.setResumeUrl(profile.getResumeUrl());
        dto.setCreatedAt(profile.getCreatedAt());

        return dto;
    }
}