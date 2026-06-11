package com.hiresphere.hiresphere.Recruiter.Service;



import com.hiresphere.hiresphere.Recruiter.DTO.GetRecruiterProfileResponseDto;
import com.hiresphere.hiresphere.Recruiter.DTO.RecruiterProfileRequestDto;
import com.hiresphere.hiresphere.Recruiter.DTO.RecruiterProfileResponseDto;




public interface RecruiterService {

	GetRecruiterProfileResponseDto getRecruiterProfile();

	
	RecruiterProfileResponseDto createRecuiterprofile(
			RecruiterProfileRequestDto recruiterProfileRequestDto);

	
	
	RecruiterProfileResponseDto updateRecruiterProfile( RecruiterProfileRequestDto recruiterProfileRequestDto);


	void deleteRecruiterProfile(); 

} 
    