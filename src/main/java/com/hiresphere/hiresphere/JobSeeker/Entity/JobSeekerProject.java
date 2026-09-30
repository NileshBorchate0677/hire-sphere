package com.hiresphere.hiresphere.JobSeeker.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class JobSeekerProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(length = 500)
    private String techStack;

    @Column(length = 255)
    private String githubUrl;

    @Column(length = 255)
    private String liveDemoUrl;

    @Column(length = 150)
    private String clientName;

    @Column(length = 150)
    private String role;

    private Integer teamSize;

    @Column(length = 50)
    private String projectStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_profile_id", nullable = false)
    private JobSeekerProfile jobSeekerProfile;
}
