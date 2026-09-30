package com.hiresphere.hiresphere.Job.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile; 

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {


    @org.springframework.data.jpa.repository.Query(
        "SELECT j FROM Job j JOIN FETCH j.recruiterProfile rp JOIN FETCH rp.user WHERE rp = :recruiterProfile")
    List<Job> findByRecruiterProfile(
            @org.springframework.data.repository.query.Param("recruiterProfile") RecruiterProfile recruiterProfile);



	@org.springframework.data.jpa.repository.Query("SELECT j FROM Job j JOIN FETCH j.recruiterProfile WHERE j.status = :status")
	List<Job> findByStatus(
	        @org.springframework.data.repository.query.Param("status") JobStatus status);

	@org.springframework.data.jpa.repository.Query("SELECT j FROM Job j JOIN FETCH j.recruiterProfile rp JOIN FETCH rp.user WHERE j.id = :id")
	java.util.Optional<Job> findByIdWithRecruiter(@org.springframework.data.repository.query.Param("id") Long id);


	void deleteByRecruiterProfile(RecruiterProfile recruiterProfile);
}
