package com.hiresphere.hiresphere.JobSeeker.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProject;

@Repository
public interface JobSeekerProjectRepository extends JpaRepository<JobSeekerProject, Long> {
    List<JobSeekerProject> findByJobSeekerProfile(JobSeekerProfile profile);
}
