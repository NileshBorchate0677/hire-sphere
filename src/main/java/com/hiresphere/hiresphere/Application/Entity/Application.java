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
        }
)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApplicationStatus status;

    @Column(length = 1000)
    private String coverLetter;

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