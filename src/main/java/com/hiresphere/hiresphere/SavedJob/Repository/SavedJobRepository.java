package com.hiresphere.hiresphere.SavedJob.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.SavedJob.Entity.SavedJob;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    @Query("SELECT s FROM SavedJob s JOIN FETCH s.job j JOIN FETCH j.recruiterProfile WHERE s.user = :user ORDER BY s.savedAt DESC")
    List<SavedJob> findByUserWithJobs(@Param("user") Users user);

    boolean existsByUserAndJob(Users user, Job job);

    Optional<SavedJob> findByUserAndJob(Users user, Job job);

    void deleteByUserAndJob(Users user, Job job);

    void deleteByUser(Users user);

    void deleteByJobIn(List<Job> jobs);
}
