package com.hiresphere.hiresphere.SavedJob.Dto;

import java.time.LocalDateTime;

import com.hiresphere.hiresphere.Job.Dto.JobResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavedJobResponseDto {
    private Long id;
    private Long jobId;
    private JobResponseDto job;
    private LocalDateTime savedAt;
}
