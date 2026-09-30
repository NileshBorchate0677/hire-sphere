package com.hiresphere.hiresphere.SavedJob.Service;

import java.util.List;

import com.hiresphere.hiresphere.SavedJob.Dto.SavedJobResponseDto;

public interface SavedJobService {
    SavedJobResponseDto saveJob(Long jobId);
    void removeSavedJob(Long jobId);
    List<SavedJobResponseDto> getMySavedJobs();
    boolean isJobSaved(Long jobId);
}
