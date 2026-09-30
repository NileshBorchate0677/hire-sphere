package com.hiresphere.hiresphere.JobSeeker.Dto;

import java.time.LocalDateTime;

import com.hiresphere.hiresphere.JobSeeker.Enums.Gender;
import com.hiresphere.hiresphere.JobSeeker.Enums.Qualification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSeekerProfileResponseDto {

    private Long jobSeekerProfileId;

    private String fullName;
    private String email;

    private Gender gender;

    private String phoneNumber;

    private String location;

    private String headline;

    private Integer experience;

    private String skills;

    private String summary;

    private Qualification highestQualification;

    private String collegeName;

    private String resumeUrl;
    private String resumeFileName;
    private String currentDesignation;
    private String currentCompany;
    private Double currentSalary;
    private Double expectedSalary;
    private String preferredLocation;
    private String course;
    private Integer passingYear;
    private String noticePeriod;
    private String githubUrl;
    private String linkedinUrl;
    private String portfolioUrl;

    // Naukri Career Profile Fields
    private String currentIndustry;
    private String department;
    private String roleCategory;
    private String desiredJobType;
    private String desiredEmploymentType;
    private String preferredWorkMode;
    private String preferredShift;

    // Naukri Personal Details Fields
    private String dateOfBirth;
    private String maritalStatus;
    private String hometown;
    private String pincode;
    private String permanentAddress;
    private String languagesKnown;
    private Boolean differentlyAbled;
    private Boolean careerBreak;

    // Child records for complete candidate dossier
    private java.util.List<JobSeekerEducationResponseDto> educations;
    private java.util.List<JobSeekerExperienceDto> experiences;
    private java.util.List<JobSeekerProjectDto> projects;

    private LocalDateTime createdAt;
}