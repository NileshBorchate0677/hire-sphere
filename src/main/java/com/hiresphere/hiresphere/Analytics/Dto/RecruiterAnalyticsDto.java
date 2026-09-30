package com.hiresphere.hiresphere.Analytics.Dto;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

/**
 * Recruiter-level analytics summary:
 * - total jobs posted
 * - total applications received
 * - application count broken down by status
 * - per-job application counts (top performing jobs)
 */
@Getter
@Builder
public class RecruiterAnalyticsDto {

    private long totalJobsPosted;
    private long openJobs;
    private long closedJobs;
    private long totalApplicationsReceived;

    /** status → count  e.g. {"PENDING": 12, "ACCEPTED": 3, "REJECTED": 5} */
    private Map<String, Long> applicationsByStatus;

    /** Per-job breakdown */
    private List<JobApplicationStatDto> perJobStats;
}
