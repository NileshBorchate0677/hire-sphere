package com.hiresphere.hiresphere.JobSeeker.Dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSeekerExperienceDto {
    private Long id;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Designation is required")
    private String designation;

    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrentJob;
    private String employmentType;
    private String department;
    private Double currentCtc;
    private String noticePeriod;
    private String responsibilities;
    private String techStack;
}
