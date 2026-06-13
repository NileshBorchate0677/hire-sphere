package com.hiresphere.hiresphere.Application.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hiresphere.hiresphere.Application.DTO.ApplicantResponseDto;
import com.hiresphere.hiresphere.Application.DTO.ApplyJobRequestDto;
import com.hiresphere.hiresphere.Application.DTO.MyApplicationResponseDto;
import com.hiresphere.hiresphere.Application.Service.ApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    
    
    
    
    // 1) Apply Job

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<MyApplicationResponseDto>
    applyJob(
            @PathVariable Long jobId,
            @Valid @RequestBody ApplyJobRequestDto dto)
    {
        MyApplicationResponseDto application =
                applicationService.applyJob(
                        jobId,
                        dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(application);
    }


    
    
    
    // 2) Get My Applications

    @GetMapping("/myApplications")
    public ResponseEntity<List<MyApplicationResponseDto>>
    getMyApplications()
    {
        List<MyApplicationResponseDto> applications =
                applicationService.getMyApplications();

        return ResponseEntity.ok(applications);
    }


    
    
    
    // 3) Withdraw Application

    @DeleteMapping("/withdraw/{applicationId}")
    public ResponseEntity<String>
    withdrawApplication(
            @PathVariable Long applicationId)
    {
        applicationService.withdrawApplication(
                applicationId);

        return ResponseEntity.ok(
                "Application withdrawn successfully");
    }


    // 4) Recruiter View Applicants

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicantResponseDto>>
    getApplicantsForJob(
            @PathVariable Long jobId)
    {
        List<ApplicantResponseDto> applicants =
                applicationService.getApplicantsForJob(
                        jobId);

        return ResponseEntity.ok(applicants);
    }

    
    
    

    // 5) Shortlist Applicant

    @PatchMapping("/{applicationId}/shortlist")
    public ResponseEntity<ApplicantResponseDto>
    shortlistApplication(
            @PathVariable Long applicationId)
    {
        ApplicantResponseDto applicant =
                applicationService
                        .shortlistApplication(
                                applicationId);

        return ResponseEntity.ok(applicant);
    }


    // 6) Accept Applicant

    @PatchMapping("/{applicationId}/accept")
    public ResponseEntity<ApplicantResponseDto>
    acceptApplication(
            @PathVariable Long applicationId)
    {
        ApplicantResponseDto applicant =
                applicationService
                        .acceptApplication(
                                applicationId);

        return ResponseEntity.ok(applicant);
    }

    
    
    
    

    // 7) Reject Applicant

    @PatchMapping("/{applicationId}/reject")
    public ResponseEntity<ApplicantResponseDto>
    rejectApplication(
            @PathVariable Long applicationId)
    {
        ApplicantResponseDto applicant =
                applicationService
                        .rejectApplication(
                                applicationId);

        return ResponseEntity.ok(applicant);
    }
}