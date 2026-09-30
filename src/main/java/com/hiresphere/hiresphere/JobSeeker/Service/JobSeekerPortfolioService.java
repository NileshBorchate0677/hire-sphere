package com.hiresphere.hiresphere.JobSeeker.Service;

import java.util.List;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerExperienceDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProjectDto;

public interface JobSeekerPortfolioService {
    // Projects
    JobSeekerProjectDto addProject(JobSeekerProjectDto dto);
    List<JobSeekerProjectDto> getMyProjects();
    JobSeekerProjectDto updateProject(Long projectId, JobSeekerProjectDto dto);
    void deleteProject(Long projectId);

    // Experiences
    JobSeekerExperienceDto addExperience(JobSeekerExperienceDto dto);
    List<JobSeekerExperienceDto> getMyExperiences();
    JobSeekerExperienceDto updateExperience(Long experienceId, JobSeekerExperienceDto dto);
    void deleteExperience(Long experienceId);
}
