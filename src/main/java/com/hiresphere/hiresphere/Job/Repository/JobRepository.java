package com.hiresphere.hiresphere.Job.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile; 

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

	List<Job> findByRecruiterProfile(
	        RecruiterProfile recruiterProfile);

	List<Job> findByStatus(
	        JobStatus status);

}
