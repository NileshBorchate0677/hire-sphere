package com.hiresphere.hiresphere.Application.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Application.DTO.ApplicantResponseDto;
import com.hiresphere.hiresphere.Application.DTO.ApplyJobRequestDto;
import com.hiresphere.hiresphere.Application.DTO.MyApplicationResponseDto;
import com.hiresphere.hiresphere.Application.Entity.Application;
import com.hiresphere.hiresphere.Application.Enums.ApplicationStatus;
import com.hiresphere.hiresphere.Application.Mapper.ApplicationMapper; 
import com.hiresphere.hiresphere.Application.Repository.ApplicationRepository;
import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Job.Repository.JobRepository;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerRepository;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;
import com.hiresphere.hiresphere.Recruiter.Repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl
        implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final UserRepository userRepository;

    private final JobRepository jobRepository;

    private final JobSeekerRepository jobSeekerRepository;
    
    private final RecruiterRepository recruiterRepository;



    // ==================================================
    // 1 Apply Job
    // ==================================================

    @Override
    public MyApplicationResponseDto applyJob(
            Long jobId,
            ApplyJobRequestDto dto)
    {

        // Get Login User

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        // Find User

        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));


        // Role Check

        if(user.getRole()
                != UserRoles.JOB_SEEKER)
        {
            throw new RuntimeException(
                    "Only Job Seeker can apply");
        }


        // Find Job Seeker Profile

        JobSeekerProfile profile =
                jobSeekerRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job Seeker Profile not found"));


        // Find Job

        Job job =
                jobRepository
                        .findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));


        // Check Job Status

        if(job.getStatus()
                != JobStatus.OPEN)
        {
            throw new RuntimeException(
                    "Job is not accepting applications");
        }


        // Check Already Applied

        if(applicationRepository
                .existsByJobAndJobSeekerProfile(
                        job,
                        profile))
        {
            throw new RuntimeException(
                    "You have already applied for this job");
        }


        // Create Application

        Application application =
                new Application();

        application.setJob(job);

        application.setJobSeekerProfile(
                profile);

        application.setCoverLetter(
                dto.getCoverLetter());

        application.setStatus(
                ApplicationStatus.APPLIED);


        // Save Application

        Application savedApplication =
                applicationRepository
                        .save(application);


        // Return Response

        return ApplicationMapper
                .mapToMyApplicationResponseDto(
                        savedApplication);
    }



    // ==================================================
    // 2 Get My Applications
    // ==================================================

    @Override
    public List<MyApplicationResponseDto>
    getMyApplications()
    {

        // Get Login User

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        // Find User

        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));


        // Role Check

        if(user.getRole()
                != UserRoles.JOB_SEEKER)
        {
            throw new RuntimeException(
                    "Only Job Seeker can view applications");
        }


        // Find Job Seeker Profile

        JobSeekerProfile profile =
                jobSeekerRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job Seeker Profile not found"));


        // Get Applications

        List<Application> applications =
                applicationRepository
                        .findByJobSeekerProfile(
                                profile);


        // Convert DTO

        List<MyApplicationResponseDto>
                applicationDtos =
                new ArrayList<>();


        for(Application application :
                applications)
        {
            applicationDtos.add(
                    ApplicationMapper
                            .mapToMyApplicationResponseDto(
                                    application));
        }


        return applicationDtos;
    }



    // ==================================================
    // 3 Withdraw Application
    // ==================================================

    @Override
    public void withdrawApplication(
            Long applicationId)
    {

        // Get Login User

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        // Find User

        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));


        // Role Check

        if(user.getRole()
                != UserRoles.JOB_SEEKER)
        {
            throw new RuntimeException(
                    "Only Job Seeker can withdraw applications");
        }


        // Find Job Seeker Profile

        JobSeekerProfile profile =
                jobSeekerRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job Seeker Profile not found"));


        // Find Application

        Application application =
                applicationRepository
                        .findByIdAndJobSeekerProfile(
                                applicationId,
                                profile)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));


        // Business Validation

        if(application.getStatus()
                == ApplicationStatus.ACCEPTED)
        {
            throw new RuntimeException(
                    "Accepted application cannot be withdrawn");
        }

        if(application.getStatus()
                == ApplicationStatus.WITHDRAWN)
        {
            throw new RuntimeException(
                    "Application already withdrawn");
        }


        // Update Status

        application.setStatus(
                ApplicationStatus.WITHDRAWN);


        // Save

        applicationRepository
                .save(application);
    }
    
    
    
    
    @Override
    public List<ApplicantResponseDto>
    getApplicantsForJob(Long jobId)
    {

        // Get Login User

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        // Find User

        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        // Role Check

        if(user.getRole()
                != UserRoles.RECRUITER)
        {
            throw new RuntimeException(
                    "Only Recruiter can view applicants");
        }

        // Find Recruiter Profile

        RecruiterProfile recruiterProfile =
                recruiterRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter Profile not found"));

        // Find Job

        Job job =
                jobRepository
                        .findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"));

        // Ownership Check

        if(!job.getRecruiterProfile()
                .getRecruiter_Profile_id()
                .equals(
                        recruiterProfile
                                .getRecruiter_Profile_id()))
        {
            throw new RuntimeException(
                    "You can only view your own applicants");
        }

        // Get Applications

        List<Application> applications =
                applicationRepository
                        .findByJob(job);

        // Convert DTO

        List<ApplicantResponseDto> applicants =
                new ArrayList<>();

        for(Application application :
                applications)
        {
            applicants.add(
                    ApplicationMapper
                            .mapToApplicantResponseDto(
                                    application));
        }

        return applicants;
    }
    
    
    
    @Override
    public ApplicantResponseDto
    shortlistApplication(Long applicationId)
    {

        // Get Login User

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        // Find User

        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        // Role Check

        if(user.getRole()
                != UserRoles.RECRUITER)
        {
            throw new RuntimeException(
                    "Only Recruiter can shortlist applicants");
        }

        // Find Recruiter Profile

        RecruiterProfile recruiterProfile =
                recruiterRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter Profile not found"));

        // Find Application

        Application application =
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));

        // Ownership Check

        if(!application.getJob()
                .getRecruiterProfile()
                .getRecruiter_Profile_id()
                .equals(
                        recruiterProfile
                                .getRecruiter_Profile_id()))
        {
            throw new RuntimeException(
                    "You can update only your own applicants");
        }

        if(application.getStatus()
                == ApplicationStatus.WITHDRAWN)
        {
            throw new RuntimeException(
                    "Withdrawn application cannot be shortlisted");
        }

        application.setStatus(
                ApplicationStatus.SHORTLISTED);

        Application updatedApplication =
                applicationRepository
                        .save(application);

        return ApplicationMapper
                .mapToApplicantResponseDto(
                        updatedApplication);
    }
    
    
    
    
    @Override
    public ApplicantResponseDto
    acceptApplication(Long applicationId)
    {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if(user.getRole()
                != UserRoles.RECRUITER)
        {
            throw new RuntimeException(
                    "Only Recruiter can accept applicants");
        }

        RecruiterProfile recruiterProfile =
                recruiterRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter Profile not found"));

        Application application =
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));

        if(!application.getJob()
                .getRecruiterProfile()
                .getRecruiter_Profile_id()
                .equals(
                        recruiterProfile
                                .getRecruiter_Profile_id()))
        {
            throw new RuntimeException(
                    "You can update only your own applicants");
        }

        if(application.getStatus()
                == ApplicationStatus.WITHDRAWN)
        {
            throw new RuntimeException(
                    "Withdrawn application cannot be accepted");
        }

        application.setStatus(
                ApplicationStatus.ACCEPTED);

        Application updatedApplication =
                applicationRepository
                        .save(application);

        return ApplicationMapper
                .mapToApplicantResponseDto(
                        updatedApplication);
    }
    
    
    
    
    @Override
    public ApplicantResponseDto
    rejectApplication(Long applicationId)
    {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        Users user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        if(user.getRole()
                != UserRoles.RECRUITER)
        {
            throw new RuntimeException(
                    "Only Recruiter can reject applicants");
        }

        RecruiterProfile recruiterProfile =
                recruiterRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter Profile not found"));

        Application application =
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found"));

        if(!application.getJob()
                .getRecruiterProfile()
                .getRecruiter_Profile_id()
                .equals(
                        recruiterProfile
                                .getRecruiter_Profile_id()))
        {
            throw new RuntimeException(
                    "You can update only your own applicants");
        }

        if(application.getStatus()
                == ApplicationStatus.WITHDRAWN)
        {
            throw new RuntimeException(
                    "Withdrawn application cannot be rejected");
        }

        application.setStatus(
                ApplicationStatus.REJECTED);

        Application updatedApplication =
                applicationRepository
                        .save(application);

        return ApplicationMapper
                .mapToApplicantResponseDto(
                        updatedApplication);
    }
}