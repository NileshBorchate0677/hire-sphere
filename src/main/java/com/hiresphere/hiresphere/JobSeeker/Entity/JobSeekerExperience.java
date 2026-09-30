package com.hiresphere.hiresphere.JobSeeker.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class JobSeekerExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String companyName;

    @Column(nullable = false, length = 100)
    private String designation;

    @Column(length = 100)
    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isCurrentJob;

    @Column(length = 50)
    private String employmentType;

    @Column(length = 150)
    private String department;

    private Double currentCtc;

    @Column(length = 60)
    private String noticePeriod;

    @Column(length = 2000)
    private String responsibilities;

    @Column(length = 500)
    private String techStack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_profile_id", nullable = false)
    private JobSeekerProfile jobSeekerProfile;
}
