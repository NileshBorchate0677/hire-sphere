package com.hiresphere.hiresphere.Job.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.hiresphere.hiresphere.Application.Entity.Application;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Job.Enums.JobType;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter 
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 3000)
    private String description;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(nullable = false)
    private Double minSalary;

    @Column(nullable = false)
    private Double maxSalary;

    @Column(nullable = false)
    private Integer experienceRequired;

    @Column(nullable = false)
    private Integer vacancies;

    @Column(nullable = false, length = 1000)
    private String requiredSkills;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column(nullable = false)
    private LocalDate applicationDeadline;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "recruiter_Profile_id",
            nullable = false
    )
    private RecruiterProfile recruiterProfile;
    
    
    
    @OneToMany(mappedBy = "job")
    private List<Application> applications;
}