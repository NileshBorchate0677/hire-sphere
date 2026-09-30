package com.hiresphere.hiresphere.JobSeeker.Service;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;

public interface ResumeStorageService {

    JobSeekerProfileResponseDto uploadResume(MultipartFile file) throws IOException;

    Path getResumeFilePath(String fileName);
}
