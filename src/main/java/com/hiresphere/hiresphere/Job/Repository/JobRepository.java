package com.hiresphere.hiresphere.Job.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.Job.Entity.Job; 

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

}
