package com.hiresphere.hiresphere.JobSeeker.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerExperience;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;

@Repository
public interface JobSeekerExperienceRepository extends JpaRepository<JobSeekerExperience, Long> {
    List<JobSeekerExperience> findByJobSeekerProfile(JobSeekerProfile profile);
}
