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
    @Column(nullable = true)
    private Qualification highestQualification;

    @Column(nullable = true, length = 200)
    private String collegeName;

    @Column(nullable = true, length = 500)
    private String resumeUrl;

    @Column(length = 255)
    private String resumeFileName;

    @Column(length = 150)
    private String currentDesignation;

    @Column(length = 150)
    private String currentCompany;

    private Double currentSalary;

    private Double expectedSalary;

    @Column(length = 500)
    private String preferredLocation;

    @Column(length = 150)
    private String course;

    private Integer passingYear;

    @Column(length = 50)
    private String noticePeriod;

    @Column(length = 255)
    private String githubUrl;

    @Column(length = 255)
    private String linkedinUrl;

    @Column(length = 255)
    private String portfolioUrl;

    // Naukri Career Profile Fields
    @Column(length = 150)
    private String currentIndustry;

    @Column(length = 150)
    private String department;

    @Column(length = 150)
    private String roleCategory;

    @Column(length = 50)
    private String desiredJobType;

    @Column(length = 50)
    private String desiredEmploymentType;

    @Column(length = 50)
    private String preferredWorkMode;

    @Column(length = 50)
    private String preferredShift;

    // Naukri Personal Details Fields
    @Column(length = 30)
    private String dateOfBirth;

    @Column(length = 50)
    private String maritalStatus;

    @Column(length = 100)
    private String hometown;

    @Column(length = 15)
    private String pincode;

    @Column(length = 500)
    private String permanentAddress;

    @Column(length = 500)
    private String languagesKnown;

    private Boolean differentlyAbled;

    private Boolean careerBreak;

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


    @OneToMany(
            mappedBy = "jobSeekerProfile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<JobSeekerEducation> educations;

    @OneToMany(
            mappedBy = "jobSeekerProfile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<JobSeekerProject> projects;

    @OneToMany(
            mappedBy = "jobSeekerProfile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<JobSeekerExperience> experiences;

    @OneToMany(mappedBy = "jobSeekerProfile")
    private List<Application> applications;
}