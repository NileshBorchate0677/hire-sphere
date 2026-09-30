package com.hiresphere.hiresphere.Analytics.Service;

import com.hiresphere.hiresphere.Analytics.Dto.RecruiterAnalyticsDto;

public interface AnalyticsService {

    /** Returns full analytics summary for the currently logged-in recruiter. */
    RecruiterAnalyticsDto getRecruiterAnalytics();
}
