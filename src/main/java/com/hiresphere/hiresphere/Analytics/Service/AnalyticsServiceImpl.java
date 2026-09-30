package com.hiresphere.hiresphere.Analytics.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiresphere.hiresphere.Analytics.Dto.JobApplicationStatDto;
import com.hiresphere.hiresphere.Analytics.Dto.RecruiterAnalyticsDto;
import com.hiresphere.hiresphere.Application.Repository.ApplicationRepository;
import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Job.Repository.JobRepository;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;
import com.hiresphere.hiresphere.Recruiter.Repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    private RecruiterProfile getLoggedInRecruiter() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != UserRoles.RECRUITER) {
            throw new RuntimeException("Only recruiter allowed");
        }
        return recruiterRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
    }

    @Override
    public RecruiterAnalyticsDto getRecruiterAnalytics() {
        RecruiterProfile recruiter = getLoggedInRecruiter();

        // ── Job counts ──────────────────────────────────────────────────────
        List<com.hiresphere.hiresphere.Job.Entity.Job> allJobs =
                jobRepository.findByRecruiterProfile(recruiter);

        long totalJobs  = allJobs.size();
        long openJobs   = allJobs.stream().filter(j -> j.getStatus() == JobStatus.OPEN).count();
        long closedJobs = allJobs.stream().filter(j -> j.getStatus() == JobStatus.CLOSED).count();

        // ── Application totals ──────────────────────────────────────────────
        long totalApplications = applicationRepository.countByRecruiter(recruiter);

        // ── Status breakdown ────────────────────────────────────────────────
        List<Object[]> statusRows = applicationRepository.countByStatusForRecruiter(recruiter);
        Map<String, Long> byStatus = new LinkedHashMap<>();
        for (Object[] row : statusRows) {
            // row[0] = ApplicationStatus enum, row[1] = Long count
            byStatus.put(row[0].toString(), (Long) row[1]);
        }

        // ── Per-job stats ────────────────────────────────────────────────────
        List<Object[]> jobRows = applicationRepository.countByJobForRecruiter(recruiter);
        List<JobApplicationStatDto> perJobStats = jobRows.stream().map(row -> {
            Long   jobId    = (Long)   row[0];
            String title    = (String) row[1];
            String status   = row[2].toString();
            Long   count    = (Long)   row[3];
            return JobApplicationStatDto.builder()
                    .jobId(jobId)
                    .jobTitle(title)
                    .status(status)
                    .applicationCount(count)
                    .build();
        }).toList();

        return RecruiterAnalyticsDto.builder()
                .totalJobsPosted(totalJobs)
                .openJobs(openJobs)
                .closedJobs(closedJobs)
                .totalApplicationsReceived(totalApplications)
                .applicationsByStatus(byStatus)
                .perJobStats(perJobStats)
                .build();
    }
}
