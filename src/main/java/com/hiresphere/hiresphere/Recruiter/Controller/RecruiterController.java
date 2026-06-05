package com.hiresphere.hiresphere.Recruiter.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiresphere.hiresphere.Recruiter.DTO.RecruiterProfileRequestDto;
import com.hiresphere.hiresphere.Recruiter.DTO.RecruiterProfileResponseDto;
import com.hiresphere.hiresphere.Recruiter.Service.RecruiterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruiter")
public class RecruiterController {
	
	private final RecruiterService recruiterService;
	
	//1) API for create recruiter
	
	@PostMapping("/recuiterProfileCreate") 
	public ResponseEntity<RecruiterProfileResponseDto> cretaeRecruiterprofile (@Valid @RequestBody
			RecruiterProfileRequestDto recruiterProfileRequestDto)
	{
		RecruiterProfileResponseDto resposeDto= recruiterService.createRecuiterprofile(recruiterProfileRequestDto);
		
		return ResponseEntity.ok(resposeDto);
	} 
	

}
