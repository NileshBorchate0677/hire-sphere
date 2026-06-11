package com.hiresphere.hiresphere.Job.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiresphere.hiresphere.Job.Dto.CreateJobRequestDto;
import com.hiresphere.hiresphere.Job.Dto.JobResponseDto;
import com.hiresphere.hiresphere.Job.Service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/Jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // 1 Create Job

    @PostMapping("/createJob")
    public ResponseEntity<JobResponseDto> createJob(
            @Valid @RequestBody CreateJobRequestDto dto)
    {
        JobResponseDto job = jobService.createJob(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    
    
    
    // 2 Get My Jobs

    @GetMapping("/getMyJobs")
    public ResponseEntity<List<JobResponseDto>>
    getMyJobs()
    {
        List<JobResponseDto> jobs = jobService.getMyJobs();

        return ResponseEntity.ok(jobs);
    }

    
    
    
    // 3 Get Job By Id

    @GetMapping("/getJob/{jobId}")
    public ResponseEntity<JobResponseDto>
    getJobById( @PathVariable Long jobId)
    {
        JobResponseDto job = jobService.getJobById(jobId);

        return ResponseEntity.ok(job);
    }

    
    
    
    
    // 4 Update Job

    @PutMapping("/updateJob/{jobId}")
    public ResponseEntity<JobResponseDto>
    updateJob(@PathVariable Long jobId, @Valid @RequestBody CreateJobRequestDto dto)
    {
        JobResponseDto job = jobService.updateJob(jobId, dto);

        return ResponseEntity.ok(job);
    }

    
    
    
    
    // 5 Delete Job

    @DeleteMapping("/deleteJob/{jobId}")
    public ResponseEntity<String>
    deleteJob( @PathVariable Long jobId)
    {
        jobService.deleteJob(jobId);

        return ResponseEntity.ok( "Job deleted successfully");
    }

    
    
    
    // 6 Close Job

    @PatchMapping("/closeJob/{jobId}")
    public ResponseEntity<JobResponseDto> closeJob(@PathVariable Long jobId)
    {
        JobResponseDto job = jobService.closeJob(jobId);

        return ResponseEntity.ok(job);
    }

    
    
    // 7 Get All Open Jobs

    @GetMapping("/getAllJobs")
    public ResponseEntity<List<JobResponseDto>>  getAllJobs()
    {
        List<JobResponseDto> jobs =jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }
}
