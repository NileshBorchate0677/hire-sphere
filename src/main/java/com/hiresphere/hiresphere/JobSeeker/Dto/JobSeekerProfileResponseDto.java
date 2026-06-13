package com.hiresphere.hiresphere.JobSeeker.Dto;

import java.time.LocalDateTime;

import com.hiresphere.hiresphere.JobSeeker.Enums.Gender;
import com.hiresphere.hiresphere.JobSeeker.Enums.Qualification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSeekerProfileResponseDto {

    private Long jobSeekerProfileId;

    private String fullName;

    private Gender gender;

    private String phoneNumber;

    private String location;

    private String headline;

    private Integer experience;

    private String skills;

    private String summary;

    private Qualification highestQualification;

    private String collegeName;

    private String resumeUrl;

    private LocalDateTime createdAt;
}