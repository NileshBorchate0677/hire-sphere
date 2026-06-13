package com.hiresphere.hiresphere.JobSeeker.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;


@Repository
public interface JobSeekerRepository
        extends JpaRepository<JobSeekerProfile, Long>{

    boolean existsByUser(Users user);

    Optional<JobSeekerProfile> findByUser(Users user);
}