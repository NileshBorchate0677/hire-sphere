package com.hiresphere.hiresphere.Application.DTO;

import java.time.LocalDateTime;

import com.hiresphere.hiresphere.Application.Enums.ApplicationStatus;
import com.hiresphere.hiresphere.JobSeeker.Enums.Qualification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicantResponseDto {

    private Long applicationId;

    private String applicantName;
    private String email;
    private String phoneNumber;
    private String location;
    private String headline;
    private String summary;
    private Integer experience;
    private Qualification highestQualification;
    private String collegeName;
    private String skills;
    private String resumeUrl;
    private String noticePeriod;
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;
    private java.util.List<com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProjectDto> projects;
    private java.util.List<com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerExperienceDto> experiences;
    private java.util.List<com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerEducationResponseDto> educations;
    private String coverLetter;

    private ApplicationStatus status;
    private LocalDateTime appliedAt;
}