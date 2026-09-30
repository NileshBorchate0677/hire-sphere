package com.hiresphere.hiresphere.Application.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.Application.Entity.Application;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;

@Repository
public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    // Check if Job Seeker already applied
    boolean existsByJobAndJobSeekerProfile(
            Job job,
            JobSeekerProfile jobSeekerProfile);

    // Check if any application references this resume URL
    boolean existsByAppliedResumeUrl(String appliedResumeUrl);

    // Get all applications of logged-in Job Seeker with job and recruiter profile fetched
    @Query("SELECT a FROM Application a JOIN FETCH a.job j JOIN FETCH j.recruiterProfile WHERE a.jobSeekerProfile = :profile")
    List<Application> findByJobSeekerProfile(
            @Param("profile") JobSeekerProfile profile);

    // Get all applicants for a Job
    @Query("SELECT DISTINCT a FROM Application a JOIN FETCH a.jobSeekerProfile p LEFT JOIN FETCH p.user WHERE a.job = :job")
    List<Application> findByJob(
            @Param("job") Job job);

    // Used for Withdraw Application
    Optional<Application> findByIdAndJobSeekerProfile(
            Long applicationId,
            JobSeekerProfile jobSeekerProfile);

    @Query("SELECT a FROM Application a JOIN FETCH a.job j JOIN FETCH j.recruiterProfile JOIN FETCH a.jobSeekerProfile p LEFT JOIN FETCH p.user WHERE a.id = :id")
    Optional<Application> findByIdWithDetails(
            @Param("id") Long id);

    void deleteByJobSeekerProfile(JobSeekerProfile jobSeekerProfile);

    void deleteByJobIn(List<Job> jobs);

    // ─── Analytics Queries ────────────────────────────────────────────────────

    /** Total application count for all jobs of a recruiter */
    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.recruiterProfile = :recruiter")
    long countByRecruiter(@Param("recruiter") RecruiterProfile recruiter);

    /**
     * Returns rows of [jobId, jobTitle, jobStatus, applicationCount]
     * for every job owned by the recruiter.
     */
    @Query("""
            SELECT a.job.id, a.job.title, a.job.status, COUNT(a)
            FROM Application a
            WHERE a.job.recruiterProfile = :recruiter
            GROUP BY a.job.id, a.job.title, a.job.status
            ORDER BY COUNT(a) DESC
            """)
    List<Object[]> countByJobForRecruiter(@Param("recruiter") RecruiterProfile recruiter);

    /**
     * Returns rows of [status, count] grouped by ApplicationStatus
     * for all applications belonging to the recruiter's jobs.
     */
    @Query("""
            SELECT a.status, COUNT(a)
            FROM Application a
            WHERE a.job.recruiterProfile = :recruiter
            GROUP BY a.status
            """)
    List<Object[]> countByStatusForRecruiter(@Param("recruiter") RecruiterProfile recruiter);
}