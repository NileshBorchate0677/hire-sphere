package com.hiresphere.hiresphere.Application.DTO;

import java.time.LocalDateTime;

import com.hiresphere.hiresphere.Application.Enums.ApplicationStatus;
import com.hiresphere.hiresphere.JobSeeker.Enums.Qualification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicantResponseDto {

    private Long applicationId;

    private String applicantName;

    private String phoneNumber;

    private String location;

    private Integer experience;

    private Qualification highestQualification;

    private String skills;

    private String resumeUrl;

    private ApplicationStatus status;

    private LocalDateTime appliedAt;
}