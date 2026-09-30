package com.hiresphere.hiresphere.JobSeeker.Service;

import java.util.List;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerEducationRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerEducationResponseDto;

public interface JobSeekerEducationService {

    JobSeekerEducationResponseDto addEducation(
            JobSeekerEducationRequestDto dto
    );

    List<JobSeekerEducationResponseDto> getEducation();

    JobSeekerEducationResponseDto updateEducation(
            Long educationId,
            JobSeekerEducationRequestDto dto
    );

    void deleteEducation(Long educationId);
}