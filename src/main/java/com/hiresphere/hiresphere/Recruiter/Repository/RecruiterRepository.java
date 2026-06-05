package com.hiresphere.hiresphere.Recruiter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;

@Repository
public interface RecruiterRepository  extends JpaRepository<RecruiterProfile, Long>{

	boolean existsByUser(Users user);
 
	boolean existsByCompanyEmail(String companyEmail);

	boolean existsByCompanyPhone(String companyPhone);
	
}
