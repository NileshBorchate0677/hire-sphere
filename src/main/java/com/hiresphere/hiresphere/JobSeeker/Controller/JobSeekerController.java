package com.hiresphere.hiresphere.JobSeeker.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Service.JobSeekerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jobseeker")
@RequiredArgsConstructor
public class JobSeekerController {

    private final JobSeekerService jobSeekerService;
    
    

    // create the JobSeekr profile API
    @PostMapping("/createProfile")
    public ResponseEntity<JobSeekerProfileResponseDto>
    createProfile(
            @Valid
            @RequestBody
            JobSeekerProfileRequestDto dto)
    {
        JobSeekerProfileResponseDto profile =
                jobSeekerService.createProfile(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profile);
    }

    
    //get Job Seeker profile
    @GetMapping("/getProfile")
    public ResponseEntity<JobSeekerProfileResponseDto>
    getProfile()
    {
        return ResponseEntity.ok(
                jobSeekerService.getProfile());
    }

    
    
    
    
    // update the job seeker profile
    @PutMapping("/updateProfile")
    public ResponseEntity<JobSeekerProfileResponseDto>
    updateProfile(
            @Valid
            @RequestBody
            JobSeekerProfileRequestDto dto)
    {
        return ResponseEntity.ok(
                jobSeekerService.updateProfile(dto));
    }

    
    
    
    //delete job seeker profile
    @DeleteMapping("/deleteProfile")
    public ResponseEntity<String>
    deleteProfile()
    {
        jobSeekerService.deleteProfile();

        return ResponseEntity.ok(
                "Job Seeker Profile Deleted Successfully");
    }
}