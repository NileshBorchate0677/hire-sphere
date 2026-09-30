package com.hiresphere.hiresphere.JobSeeker.Entity;

import com.hiresphere.hiresphere.JobSeeker.Enums.EducationLevel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "job_seeker_education")
public class JobSeekerEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationId;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EducationLevel educationLevel;


    @Column(nullable = false, length = 150)
    private String degree;


    @Column(nullable = false, length = 200)
    private String institution;


    @Column(length = 200)
    private String university;


    @Column(nullable = false)
    private Integer passingYear;


    @Column(nullable = false)
    private Double score;


    @Column(nullable = false, length = 20)
    private String scoreType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "job_seeker_profile_id",
            nullable = false
    )
    private JobSeekerProfile jobSeekerProfile;
}