package com.hiresphere.hiresphere.JobSeeker.Service;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;

public interface JobSeekerService {

    JobSeekerProfileResponseDto createProfile(
            JobSeekerProfileRequestDto dto);

    
    JobSeekerProfileResponseDto getProfile();

    
    JobSeekerProfileResponseDto updateProfile(
            JobSeekerProfileRequestDto dto);
    
    

    void deleteProfile();
}