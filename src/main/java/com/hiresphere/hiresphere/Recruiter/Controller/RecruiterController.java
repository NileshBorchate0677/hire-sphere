package com.hiresphere.hiresphere.Recruiter.Controller;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiresphere.hiresphere.Recruiter.DTO.GetRecruiterProfileResponseDto;
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
		RecruiterProfileResponseDto createRecruiterProfile= recruiterService.createRecuiterprofile(recruiterProfileRequestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(createRecruiterProfile);
	} 
	
	//2) API for the get Recruiter profile
	
	@GetMapping("/getRecruiterProfile")
	public ResponseEntity<GetRecruiterProfileResponseDto> getRecruiterProfile()
	{
		GetRecruiterProfileResponseDto getProfile= recruiterService.getRecruiterProfile();
		return ResponseEntity.ok(getProfile);
	}
	
	
	
	//3} API for the Update the Recruiter profile 
	@PutMapping("/updateProfile")
	public ResponseEntity<RecruiterProfileResponseDto> updatProfile(@RequestBody @Valid RecruiterProfileRequestDto recruiterProfileRequestDto)
	{
		RecruiterProfileResponseDto savedProfile = recruiterService.updateRecruiterProfile(recruiterProfileRequestDto);
		return ResponseEntity.ok(savedProfile); 
	}
	
	
	// 4) Delete Recruiter profile
	
	@DeleteMapping("/deleteProfile")
	public ResponseEntity<String>deleteRecruiterProfile() 
	{
	    recruiterService.deleteRecruiterProfile();
 
	    return ResponseEntity.ok(
	            "Recruiter Profile Deleted Successfully");
	}
 
 
} 
