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

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Service.JobSeekerService;
import com.hiresphere.hiresphere.JobSeeker.Service.ResumeStorageService;
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
	private final JobSeekerService jobSeekerService;
	private final ResumeStorageService resumeStorageService;
	
	//1) API for create recruiter
	
	@PostMapping({"/recuiterProfileCreate", "/createProfile", "/recruiterProfileCreate"}) 
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

	// 5) Naukri Resdex-style Recruiter Candidate Talent Search
	@GetMapping("/candidates/search")
	public ResponseEntity<List<JobSeekerProfileResponseDto>> searchCandidates(
			@RequestParam(required = false) String skill,
			@RequestParam(required = false) String location,
			@RequestParam(required = false) Integer minExp,
			@RequestParam(required = false) Integer maxExp,
			@RequestParam(required = false) String company,
			@RequestParam(required = false) String designation,
			@RequestParam(required = false) String noticePeriod,
			@RequestParam(required = false) String workMode,
			@RequestParam(required = false) Double maxExpectedSalary
	) {
		return ResponseEntity.ok(
				jobSeekerService.searchCandidates(
						skill,
						location,
						minExp,
						maxExp,
						company,
						designation,
						noticePeriod,
						workMode,
						maxExpectedSalary
				)
		);
	}

	// 6) Get Full Candidate Profile Dossier by Candidate Profile ID
	@GetMapping("/candidates/{profileId}")
	public ResponseEntity<JobSeekerProfileResponseDto> getCandidateProfileById(@PathVariable Long profileId) {
		return ResponseEntity.ok(jobSeekerService.getCandidateProfileById(profileId));
	}

	// 7) Download Candidate Resume for Recruiter
	@GetMapping("/resume/download/{fileName:.+}")
	public ResponseEntity<org.springframework.core.io.Resource> downloadCandidateResume(@PathVariable String fileName) {
		try {
			java.nio.file.Path filePath = resumeStorageService.getResumeFilePath(fileName);
			org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());

			if (!resource.exists() || !resource.isReadable()) {
				return ResponseEntity.notFound().build();
			}

			long contentLength = -1;
			try {
				contentLength = resource.contentLength();
			} catch (Exception ignored) {
			}

			var builder = ResponseEntity.ok()
					.contentType(org.springframework.http.MediaType.APPLICATION_PDF)
					.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
					.header(org.springframework.http.HttpHeaders.CACHE_CONTROL, "public, max-age=3600");

			if (contentLength > 0) {
				builder.contentLength(contentLength);
			}

			return builder.body(resource);

		} catch (java.net.MalformedURLException | SecurityException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
