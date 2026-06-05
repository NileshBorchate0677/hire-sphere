package com.hiresphere.hiresphere.Recruiter.Entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Job.Entity.Job;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class RecruiterProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recruiter_Profile_id;
	
	@Column(nullable = false, length = 200)
	private String companyName;
	
	@Column(nullable = false, length = 1000)
	private String companyDescription;

	@Column(nullable = false)
	private String website;

	@Column(nullable = false)
	private String location;

	@Column(nullable = false)
	private String industry;

	@Column(nullable = false)
	private Integer companySize; 
	
	@Column(nullable = false, unique = true)
	private String companyEmail;

	@Column(nullable = false, unique = true)
	private String companyPhone;
	

	@CreationTimestamp
	@Column(updatable = false) 
	private LocalDateTime createdAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private Users user;

	// relation to Job entity 
	@OneToMany( mappedBy = "recruiterProfile")
	private List<Job> jobs;
	
}
