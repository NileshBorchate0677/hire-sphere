package com.hiresphere.hiresphere.Application.DTO;

import java.time.LocalDateTime;

import com.hiresphere.hiresphere.Application.Enums.ApplicationStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyApplicationResponseDto {

    private Long applicationId;

    private Long jobId;

    private String jobTitle;

    private String companyName;

    private ApplicationStatus status;

    private LocalDateTime appliedAt;
}