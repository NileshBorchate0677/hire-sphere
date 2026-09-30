package com.hiresphere.hiresphere.JobSeeker.Dto;

import com.hiresphere.hiresphere.JobSeeker.Enums.EducationLevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSeekerEducationResponseDto {

    private Long educationId;

    private EducationLevel educationLevel;
 
    private String degree;

    private String institution;

    private String university;

    private Integer passingYear;

    private Double score;

    private String scoreType;
}