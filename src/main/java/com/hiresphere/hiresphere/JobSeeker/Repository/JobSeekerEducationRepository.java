package com.hiresphere.hiresphere.JobSeeker.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerEducation;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;

@Repository
public interface JobSeekerEducationRepository
        extends JpaRepository<JobSeekerEducation, Long> {

    List<JobSeekerEducation> findByJobSeekerProfile(
            JobSeekerProfile jobSeekerProfile
    );

    Optional<JobSeekerEducation>
    findByEducationIdAndJobSeekerProfile(
            Long educationId,
            JobSeekerProfile jobSeekerProfile
    );
}