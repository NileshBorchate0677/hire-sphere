package com.hiresphere.hiresphere.JobSeeker.Dto;

import com.hiresphere.hiresphere.JobSeeker.Enums.Gender;
import com.hiresphere.hiresphere.JobSeeker.Enums.Qualification;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSeekerProfileRequestDto {

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100)
    private String fullName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid phone number"
    )
    private String phoneNumber;

    @NotBlank(message = "Location is required")
    @Size(max = 100)
    private String location;

    @NotBlank(message = "Headline is required")
    @Size(min = 5, max = 150)
    private String headline;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 50, message = "Experience cannot exceed 50 years")
    private Integer experience;

    @NotBlank(message = "Skills are required")
    @Size(min = 3, max = 1000)
    private String skills;

    @Size(max = 2000)
    private String summary;

    private Qualification highestQualification;

    @Size(max = 200)
    private String collegeName;

    @Size(max = 150)
    private String course;

    private Integer passingYear;

    @Size(max = 150)
    private String currentDesignation;

    @Size(max = 150)
    private String currentCompany;

    @DecimalMin(value = "0.0", message = "Current salary cannot be negative")
    private Double currentSalary;

    @DecimalMin(value = "0.0", message = "Expected salary cannot be negative")
    private Double expectedSalary;

    @Size(max = 500)
    private String preferredLocation;

    @Size(max = 500)
    private String resumeUrl;

    @Size(max = 255)
    private String resumeFileName;

    @Size(max = 50)
    private String noticePeriod;

    @Size(max = 255)
    private String githubUrl;

    @Size(max = 255)
    private String linkedinUrl;

    @Size(max = 255)
    private String portfolioUrl;

    // Naukri Career Profile Fields
    @Size(max = 150)
    private String currentIndustry;

    @Size(max = 150)
    private String department;

    @Size(max = 150)
    private String roleCategory;

    @Size(max = 50)
    private String desiredJobType;

    @Size(max = 50)
    private String desiredEmploymentType;

    @Size(max = 50)
    private String preferredWorkMode;

    @Size(max = 50)
    private String preferredShift;

    // Naukri Personal Details Fields
    @Size(max = 30)
    private String dateOfBirth;

    @Size(max = 50)
    private String maritalStatus;

    @Size(max = 100)
    private String hometown;

    @Size(max = 15)
    @Pattern(regexp = "^$|^[1-9][0-9]{5}$", message = "Pincode must be a valid 6-digit Indian postal code")
    private String pincode;

    @Size(max = 500)
    private String permanentAddress;

    @Size(max = 500)
    private String languagesKnown;

    private Boolean differentlyAbled;

    private Boolean careerBreak;
}