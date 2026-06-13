package com.hiresphere.hiresphere.Application.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.Application.Entity.Application;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;

@Repository
public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    // Check if Job Seeker already applied
    boolean existsByJobAndJobSeekerProfile(
            Job job,
            JobSeekerProfile jobSeekerProfile);

    // Get all applications of logged-in Job Seeker
    List<Application> findByJobSeekerProfile(
            JobSeekerProfile jobSeekerProfile);

    // Get all applicants for a Job
    List<Application> findByJob(
            Job job);

    // Used for Withdraw Application
    Optional<Application> findByIdAndJobSeekerProfile(
            Long applicationId,
            JobSeekerProfile jobSeekerProfile);
}