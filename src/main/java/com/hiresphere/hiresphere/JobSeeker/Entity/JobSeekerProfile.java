package com.hiresphere.hiresphere.JobSeeker.Entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.hiresphere.hiresphere.Application.Entity.Application;
import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.JobSeeker.Enums.Gender;
import com.hiresphere.hiresphere.JobSeeker.Enums.Qualification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class JobSeekerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobSeekerProfileId;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(nullable = false, length = 150)
    private String headline;

    @Column(nullable = false)
    private Integer experience;

    @Column(nullable = false, length = 1000)
    private String skills;

    @Column(length = 2000)
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Qualification highestQualification;

    @Column(nullable = false, length = 200)
    private String collegeName;

    @Column(nullable = false, length = 500)
    private String resumeUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id", 
            nullable = false,
            unique = true
    )
    private Users user;
    
    @OneToMany(mappedBy = "jobSeekerProfile")
    private List<Application> applications;
}