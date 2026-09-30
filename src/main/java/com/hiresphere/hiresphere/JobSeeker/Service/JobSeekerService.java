package com.hiresphere.hiresphere.JobSeeker.Service;

import java.util.List;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;

public interface JobSeekerService {

    JobSeekerProfileResponseDto createProfile(
            JobSeekerProfileRequestDto dto);

    JobSeekerProfileResponseDto getProfile();

    JobSeekerProfileResponseDto updateProfile(
            JobSeekerProfileRequestDto dto);

    void deleteProfile();

    List<JobSeekerProfileResponseDto> searchCandidates(
            String skill,
            String location,
            Integer minExp,
            Integer maxExp,
            String company,
            String designation,
            String noticePeriod,
            String workMode,
            Double maxExpectedSalary
    );

    JobSeekerProfileResponseDto getCandidateProfileById(Long candidateProfileId);
}