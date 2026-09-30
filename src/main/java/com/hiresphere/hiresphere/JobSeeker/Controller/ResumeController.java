package com.hiresphere.hiresphere.JobSeeker.Controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Service.ResumeStorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jobseeker/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeStorageService resumeStorageService;

    // 1. Upload / Replace Resume PDF
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JobSeekerProfileResponseDto> uploadResume(
            @RequestParam("file") MultipartFile file) throws IOException {

        JobSeekerProfileResponseDto profile = resumeStorageService.uploadResume(file);
        return ResponseEntity.ok(profile);
    }

    // 2. Download / View Resume PDF
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadResume(@PathVariable String fileName) {
        try {
            Path filePath = resumeStorageService.getResumeFilePath(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            long contentLength = -1;
            try {
                contentLength = resource.contentLength();
            } catch (Exception ignored) {
            }

            var builder = ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600");

            if (contentLength > 0) {
                builder.contentLength(contentLength);
            }

            return builder.body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
