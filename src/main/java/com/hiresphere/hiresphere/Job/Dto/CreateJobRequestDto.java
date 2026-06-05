package com.hiresphere.hiresphere.Job.Dto;

import java.time.LocalDate;

import com.hiresphere.hiresphere.Job.Enums.JobType;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateJobRequestDto {

    @NotBlank(message = "Job title is required")
    @Size(min = 5, max = 150)
    private String title;

    @NotBlank(message = "Job description is required")
    @Size(min = 50, max = 3000)
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Minimum salary is required")
    @Positive(message = "Minimum salary must be positive")
    private Double minSalary;

    @NotNull(message = "Maximum salary is required")
    @Positive(message = "Maximum salary must be positive")
    private Double maxSalary;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 50, message = "Experience cannot exceed 50 years")
    private Integer experienceRequired;

    @NotNull(message = "Vacancies are required")
    @Min(value = 1, message = "Vacancies must be at least 1")
    private Integer vacancies;

    @NotBlank(message = "Required skills are required")
    @Size(min = 3, max = 1000)
    private String requiredSkills;

    @NotNull(message = "Job type is required")
    private JobType jobType;

    @NotNull(message = "Application deadline is required")
    @Future(message = "Deadline must be a future date")
    private LocalDate applicationDeadline;
}