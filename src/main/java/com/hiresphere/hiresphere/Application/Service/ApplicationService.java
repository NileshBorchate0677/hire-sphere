package com.hiresphere.hiresphere.Application.Service;

import java.util.List;

import com.hiresphere.hiresphere.Application.DTO.ApplicantResponseDto;
import com.hiresphere.hiresphere.Application.DTO.ApplyJobRequestDto;
import com.hiresphere.hiresphere.Application.DTO.MyApplicationResponseDto;

public interface ApplicationService {

    // Job Seeker

    MyApplicationResponseDto applyJob(
            Long jobId,
            ApplyJobRequestDto dto);

    List<MyApplicationResponseDto>
    getMyApplications();

    void withdrawApplication(
            Long applicationId);



    // Recruiter

    List<ApplicantResponseDto>
    getApplicantsForJob(
            Long jobId);

    ApplicantResponseDto
    shortlistApplication(
            Long applicationId);

    ApplicantResponseDto
    acceptApplication(
            Long applicationId);

    ApplicantResponseDto
    rejectApplication(
            Long applicationId);
}