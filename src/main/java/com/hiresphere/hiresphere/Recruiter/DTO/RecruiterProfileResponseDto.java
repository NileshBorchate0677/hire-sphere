package com.hiresphere.hiresphere.Recruiter.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RecruiterProfileResponseDto { 

    private Long id;

    private String companyName;

    private String companyDescription;

    private String location;

    private String industry;

    private String companyEmail;

    private String companyPhone;

    private String website;

    private Integer companySize;

    private LocalDateTime createdAt;
}