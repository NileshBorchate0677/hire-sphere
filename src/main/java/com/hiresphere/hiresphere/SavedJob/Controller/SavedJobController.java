package com.hiresphere.hiresphere.SavedJob.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hiresphere.hiresphere.SavedJob.Dto.SavedJobResponseDto;
import com.hiresphere.hiresphere.SavedJob.Service.SavedJobService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/saved-jobs")
@RequiredArgsConstructor
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping("/{jobId}")
    public ResponseEntity<SavedJobResponseDto> saveJob(@PathVariable Long jobId) {
        SavedJobResponseDto response = savedJobService.saveJob(jobId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Map<String, String>> removeSavedJob(@PathVariable Long jobId) {
        savedJobService.removeSavedJob(jobId);
        return ResponseEntity.ok(Map.of("message", "Job removed from saved list successfully"));
    }

    @GetMapping("/my")
    public ResponseEntity<List<SavedJobResponseDto>> getMySavedJobs() {
        return ResponseEntity.ok(savedJobService.getMySavedJobs());
    }

    @GetMapping("/check/{jobId}")
    public ResponseEntity<Map<String, Boolean>> isJobSaved(@PathVariable Long jobId) {
        boolean saved = savedJobService.isJobSaved(jobId);
        return ResponseEntity.ok(Map.of("isSaved", saved));
    }
}
