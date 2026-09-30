package com.hiresphere.hiresphere.JobSeeker.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerExperienceDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProjectDto;
import com.hiresphere.hiresphere.JobSeeker.Service.JobSeekerPortfolioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jobseeker/portfolio")
@RequiredArgsConstructor
public class JobSeekerPortfolioController {

    private final JobSeekerPortfolioService portfolioService;

    // ================= PROJECTS =================

    @PostMapping("/projects")
    public ResponseEntity<JobSeekerProjectDto> addProject(@Valid @RequestBody JobSeekerProjectDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(portfolioService.addProject(dto));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<JobSeekerProjectDto>> getMyProjects() {
        return ResponseEntity.ok(portfolioService.getMyProjects());
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<JobSeekerProjectDto> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody JobSeekerProjectDto dto) {
        return ResponseEntity.ok(portfolioService.updateProject(projectId, dto));
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Map<String, String>> deleteProject(@PathVariable Long projectId) {
        portfolioService.deleteProject(projectId);
        return ResponseEntity.ok(Map.of("message", "Project deleted successfully"));
    }

    // ================= EXPERIENCES =================

    @PostMapping("/experiences")
    public ResponseEntity<JobSeekerExperienceDto> addExperience(@Valid @RequestBody JobSeekerExperienceDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(portfolioService.addExperience(dto));
    }

    @GetMapping("/experiences")
    public ResponseEntity<List<JobSeekerExperienceDto>> getMyExperiences() {
        return ResponseEntity.ok(portfolioService.getMyExperiences());
    }

    @PutMapping("/experiences/{experienceId}")
    public ResponseEntity<JobSeekerExperienceDto> updateExperience(
            @PathVariable Long experienceId,
            @Valid @RequestBody JobSeekerExperienceDto dto) {
        return ResponseEntity.ok(portfolioService.updateExperience(experienceId, dto));
    }

    @DeleteMapping("/experiences/{experienceId}")
    public ResponseEntity<Map<String, String>> deleteExperience(@PathVariable Long experienceId) {
        portfolioService.deleteExperience(experienceId);
        return ResponseEntity.ok(Map.of("message", "Experience deleted successfully"));
    }
}
