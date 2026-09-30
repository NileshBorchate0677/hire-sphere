package com.hiresphere.hiresphere.JobSeeker.Service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerEducationRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerEducationResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerEducation;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;
import com.hiresphere.hiresphere.JobSeeker.Mapper.JobSeekerEducationMapper;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerEducationRepository;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobSeekerEducationServiceImpl
        implements JobSeekerEducationService {


    private final UserRepository userRepository;

    private final JobSeekerRepository jobSeekerRepository;

    private final JobSeekerEducationRepository
            educationRepository;


    private JobSeekerProfile
    getLoggedInJobSeekerProfile() {

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
                                        "User Not Found"));


        if (user.getRole() != UserRoles.JOB_SEEKER) {

            throw new RuntimeException(
                    "Only Job Seeker Allowed");
        }


        return jobSeekerRepository
                .findByUser(user)
                .orElseGet(() -> {
                    JobSeekerProfile base = new JobSeekerProfile();
                    base.setUser(user);
                    base.setFullName(user.getName() != null && !user.getName().isBlank() ? user.getName() : "Candidate");
                    base.setGender(com.hiresphere.hiresphere.JobSeeker.Enums.Gender.MALE);
                    base.setPhoneNumber("9000000000");
                    base.setLocation("Pune");
                    base.setHeadline("Software Engineering Candidate");
                    base.setExperience(0);
                    base.setSkills("Java, Spring Boot, React.js, SQL");
                    base.setHighestQualification(com.hiresphere.hiresphere.JobSeeker.Enums.Qualification.BTECH);
                    base.setCollegeName("Savitribai Phule Pune University (SPPU)");
                    return jobSeekerRepository.save(base);
                });
    }


    // =====================================================
    // ADD EDUCATION
    // =====================================================

    @Override
    public JobSeekerEducationResponseDto
    addEducation(
            JobSeekerEducationRequestDto dto) {

        JobSeekerProfile profile =
                getLoggedInJobSeekerProfile();


        JobSeekerEducation education =
                JobSeekerEducationMapper
                        .mapToEntity(dto);


        education.setJobSeekerProfile(profile);


        JobSeekerEducation saved =
                educationRepository.save(education);


        return JobSeekerEducationMapper
                .mapToResponseDto(saved);
    }


    // =====================================================
    // GET EDUCATION
    // =====================================================

    @Override
    public List<JobSeekerEducationResponseDto>
    getEducation() {

        JobSeekerProfile profile =
                getLoggedInJobSeekerProfile();


        return educationRepository
                .findByJobSeekerProfile(profile)
                .stream()
                .map(
                        JobSeekerEducationMapper
                                ::mapToResponseDto
                )
                .toList();
    }


    // =====================================================
    // UPDATE EDUCATION
    // =====================================================

    @Override
    public JobSeekerEducationResponseDto
    updateEducation(
            Long educationId,
            JobSeekerEducationRequestDto dto) {

        JobSeekerProfile profile =
                getLoggedInJobSeekerProfile();


        JobSeekerEducation education =
                educationRepository
                        .findByEducationIdAndJobSeekerProfile(
                                educationId,
                                profile
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Education Not Found"));


        education.setEducationLevel(
                dto.getEducationLevel());

        education.setDegree(
                dto.getDegree());

        education.setInstitution(
                dto.getInstitution());

        education.setUniversity(
                dto.getUniversity());

        education.setPassingYear(
                dto.getPassingYear());

        education.setScore(
                dto.getScore());

        education.setScoreType(
                dto.getScoreType());


        JobSeekerEducation updated =
                educationRepository.save(
                        education);


        return JobSeekerEducationMapper
                .mapToResponseDto(updated);
    }


    // =====================================================
    // DELETE EDUCATION
    // =====================================================

    @Override
    public void deleteEducation(
            Long educationId) {

        JobSeekerProfile profile =
                getLoggedInJobSeekerProfile();


        JobSeekerEducation education =
                educationRepository
                        .findByEducationIdAndJobSeekerProfile(
                                educationId,
                                profile
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Education Not Found"));


        educationRepository.delete(education);
    }
}