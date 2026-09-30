package com.hiresphere.hiresphere.Analytics.Dto;

import lombok.Builder;
import lombok.Getter;

/** Per-job application count stat row. */
@Getter
@Builder
public class JobApplicationStatDto {

    private Long jobId;
    private String jobTitle;
    private String status;           // job status (OPEN / CLOSED)
    private long applicationCount;
}
