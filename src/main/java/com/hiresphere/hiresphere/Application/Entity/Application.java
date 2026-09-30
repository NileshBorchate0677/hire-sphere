package com.hiresphere.hiresphere.Application.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.hiresphere.hiresphere.Application.Enums.ApplicationStatus;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_job_seeker_application",
                        columnNames = {
                                "job_id",
                                "job_seeker_profile_id"
                        }
                )
        },
        indexes = {
                @Index(name = "idx_app_job", columnList = "job_id"),
                @Index(name = "idx_app_seeker", columnList = "job_seeker_profile_id"),
                @Index(name = "idx_app_status", columnList = "status")
        }
)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApplicationStatus status;

    @Column(length = 1000)
    private String coverLetter;

    @Column(length = 500)
    private String appliedResumeUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "job_id",
            nullable = false
    )
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "job_seeker_profile_id",
            nullable = false
    )
    private JobSeekerProfile jobSeekerProfile;
}