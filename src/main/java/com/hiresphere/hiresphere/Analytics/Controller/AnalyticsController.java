package com.hiresphere.hiresphere.Analytics.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiresphere.hiresphere.Analytics.Dto.RecruiterAnalyticsDto;
import com.hiresphere.hiresphere.Analytics.Service.AnalyticsService;

import lombok.RequiredArgsConstructor;

/**
 * Analytics endpoints (RECRUITER role only — enforced at service level).
 *
 * GET /analytics/recruiter  → full dashboard summary
 */
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * Returns a full analytics summary for the logged-in recruiter:
     * <ul>
     *   <li>Total / open / closed jobs</li>
     *   <li>Total applications received</li>
     *   <li>Application counts grouped by status</li>
     *   <li>Per-job application breakdown (sorted by highest applications first)</li>
     * </ul>
     */
    @GetMapping("/recruiter")
    public ResponseEntity<RecruiterAnalyticsDto> getRecruiterAnalytics() {
        return ResponseEntity.ok(analyticsService.getRecruiterAnalytics());
    }
}
