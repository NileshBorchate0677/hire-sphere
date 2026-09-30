package com.hiresphere.hiresphere.JobSeeker.Dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSeekerProjectDto {
    private Long id;

    @NotBlank(message = "Project title is required")
    private String title;

    private String description;
    private String techStack;
    private String githubUrl;
    private String liveDemoUrl;
    private String clientName;
    private String role;
    private Integer teamSize;
    private String projectStatus;
    private LocalDate startDate;
    private LocalDate endDate;
}
