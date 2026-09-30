package com.hiresphere.hiresphere.Recruiter.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;

@Repository
public interface RecruiterRepository extends JpaRepository<RecruiterProfile, Long> {

    boolean existsByUser(Users user);

    // JOIN FETCH user to avoid LazyInitializationException when mapper
    // accesses profile.getUser().getName() outside a Hibernate session.
    @Query("SELECT rp FROM RecruiterProfile rp JOIN FETCH rp.user WHERE rp.user = :user")
    Optional<RecruiterProfile> findByUser(@Param("user") Users user);
}