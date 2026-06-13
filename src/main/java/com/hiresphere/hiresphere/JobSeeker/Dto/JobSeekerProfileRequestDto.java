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

    @NotNull(message = "Qualification is required")
    private Qualification highestQualification;

    @NotBlank(message = "College name is required")
    @Size(max = 200)
    private String collegeName;

    @NotBlank(message = "Resume URL is required")
    @Size(max = 500)
    private String resumeUrl;
}