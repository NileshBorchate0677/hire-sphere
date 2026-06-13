package com.hiresphere.hiresphere.JobSeeker.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProfileResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;
import com.hiresphere.hiresphere.JobSeeker.Mapper.JobSeekerMapper;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobSeekerServiceImpl
        implements JobSeekerService {

    private final UserRepository userRepository;

    private final JobSeekerRepository
            jobSeekerRepository;

    
    
    
    // Create Profile

    @Override
    public JobSeekerProfileResponseDto
    createProfile(
            JobSeekerProfileRequestDto dto)
    {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        Users user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"));

        if(user.getRole()
                != UserRoles.JOB_SEEKER)
        {
            throw new RuntimeException(
                    "Only Job Seeker Can Create Profile");
        }

        if(jobSeekerRepository
                .existsByUser(user))
        {
            throw new RuntimeException(
                    "Profile Already Exists");
        }

        JobSeekerProfile profile =
                JobSeekerMapper
                        .mapToJobSeekerProfile(dto);

        profile.setUser(user);

        JobSeekerProfile savedProfile =
                jobSeekerRepository.save(profile);

        return JobSeekerMapper
                .mapToJobSeekerProfileResponseDto(
                        savedProfile);
    }
    
    
    
    
    
    
    

    // Get Profile

    @Override
    public JobSeekerProfileResponseDto
    getProfile()
    {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        Users user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"));

        if(user.getRole()
                != UserRoles.JOB_SEEKER)
        {
            throw new RuntimeException(
                    "Only Job Seeker Allowed");
        }

        JobSeekerProfile profile =
                jobSeekerRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profile Not Found"));

        return JobSeekerMapper
                .mapToJobSeekerProfileResponseDto(
                        profile);
    }
    
    
    
    
    
    

    // Update Profile

    @Override
    public JobSeekerProfileResponseDto
    updateProfile(
            JobSeekerProfileRequestDto dto)
    {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        Users user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"));

        if(user.getRole()
                != UserRoles.JOB_SEEKER)
        {
            throw new RuntimeException(
                    "Only Job Seeker Allowed");
        }

        JobSeekerProfile profile =
                jobSeekerRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profile Not Found"));

        profile.setFullName(
                dto.getFullName());

        profile.setGender(
                dto.getGender());

        profile.setPhoneNumber(
                dto.getPhoneNumber());

        profile.setLocation(
                dto.getLocation());

        profile.setHeadline(
                dto.getHeadline());

        profile.setExperience(
                dto.getExperience());

        profile.setSkills(
                dto.getSkills());

        profile.setSummary(
                dto.getSummary());

        profile.setHighestQualification(
                dto.getHighestQualification());

        profile.setCollegeName(
                dto.getCollegeName());

        profile.setResumeUrl(
                dto.getResumeUrl());

        JobSeekerProfile updatedProfile =
                jobSeekerRepository.save(profile);

        return JobSeekerMapper
                .mapToJobSeekerProfileResponseDto(
                        updatedProfile);
    }
    
    
    
    

    // Delete Profile

    @Override
    public void deleteProfile()
    {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        Users user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User Not Found"));

        if(user.getRole()
                != UserRoles.JOB_SEEKER)
        {
            throw new RuntimeException(
                    "Only Job Seeker Allowed");
        }

        JobSeekerProfile profile =
                jobSeekerRepository
                        .findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profile Not Found"));

        jobSeekerRepository.delete(profile);
    }
}