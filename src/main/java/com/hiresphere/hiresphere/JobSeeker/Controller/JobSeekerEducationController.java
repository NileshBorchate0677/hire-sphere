package com.hiresphere.hiresphere.JobSeeker.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerEducationRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerEducationResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Service.JobSeekerEducationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jobseeker/education")
@RequiredArgsConstructor
public class JobSeekerEducationController {


    private final JobSeekerEducationService
            educationService;


    // =====================================================
    // ADD EDUCATION
    // =====================================================

    @PostMapping
    public ResponseEntity<JobSeekerEducationResponseDto>
    addEducation(
            @Valid
            @RequestBody
            JobSeekerEducationRequestDto dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        educationService
                                .addEducation(dto)
                );
    }


    // =====================================================
    // GET ALL EDUCATION
    // =====================================================

    @GetMapping
    public ResponseEntity<
            List<JobSeekerEducationResponseDto>>
    getEducation() {

        return ResponseEntity.ok(
                educationService.getEducation()
        );
    }


    // =====================================================
    // UPDATE EDUCATION
    // =====================================================

    @PutMapping("/{educationId}")
    public ResponseEntity<JobSeekerEducationResponseDto>
    updateEducation(
            @PathVariable Long educationId,
            @Valid
            @RequestBody
            JobSeekerEducationRequestDto dto) {

        return ResponseEntity.ok(
                educationService.updateEducation(
                        educationId,
                        dto
                )
        );
    }


    // =====================================================
    // DELETE EDUCATION
    // =====================================================

    @DeleteMapping("/{educationId}")
    public ResponseEntity<String>
    deleteEducation(
            @PathVariable Long educationId) {

        educationService.deleteEducation(
                educationId);

        return ResponseEntity.ok(
                "Education deleted successfully"
        );
    }
}