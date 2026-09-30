package com.hiresphere.hiresphere.JobSeeker.Dto;

import com.hiresphere.hiresphere.JobSeeker.Enums.EducationLevel;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSeekerEducationRequestDto {

    @NotNull(message = "Education level is required")
    private EducationLevel educationLevel;


    @NotBlank(message = "Degree is required")
    @Size(max = 150)
    private String degree;


    @NotBlank(message = "Institution is required")
    @Size(max = 200)
    private String institution;


    @Size(max = 200)
    private String university;


    @NotNull(message = "Passing year is required")
    @Min(value = 1950, message = "Invalid passing year")
    @Max(value = 2100, message = "Invalid passing year")
    private Integer passingYear;


    @NotNull(message = "Score is required")
    @DecimalMin(value = "0.0", message = "Score cannot be negative")
    private Double score;


    @NotBlank(message = "Score type is required")
    @Size(max = 20)
    private String scoreType;
}