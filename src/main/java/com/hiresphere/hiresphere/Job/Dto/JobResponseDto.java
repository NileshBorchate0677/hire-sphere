package com.hiresphere.hiresphere.Job.Dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Job.Enums.JobType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobResponseDto {

    private Long id;
    
    private String companyName;

    private String title;

    private String description;

    private String location;

    private Double minSalary;

    private Double maxSalary;

    private Integer experienceRequired;

    private Integer vacancies;

    private String requiredSkills;

    private JobType jobType;

    private JobStatus status;

    private LocalDate applicationDeadline;

    private LocalDateTime createdAt;

}