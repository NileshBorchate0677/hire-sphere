package com.hiresphere.hiresphere.JobSeeker.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;


@Repository
public interface JobSeekerRepository
        extends JpaRepository<JobSeekerProfile, Long>, JpaSpecificationExecutor<JobSeekerProfile> {

    boolean existsByUser(Users user);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM JobSeekerProfile p JOIN FETCH p.user WHERE p.user = :user")
    Optional<JobSeekerProfile> findByUser(@org.springframework.data.repository.query.Param("user") Users user);
}