package com.hiresphere.hiresphere.JobSeeker.Service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerExperienceDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerProjectDto;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerExperience;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProject;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerExperienceRepository;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerProjectRepository;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobSeekerPortfolioServiceImpl implements JobSeekerPortfolioService {

    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final JobSeekerProjectRepository projectRepository;
    private final JobSeekerExperienceRepository experienceRepository;

    private JobSeekerProfile getLoggedInJobSeekerProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (user.getRole() != UserRoles.JOB_SEEKER) {
            throw new RuntimeException("Only Job Seeker Allowed");
        }

        return jobSeekerRepository.findByUser(user)
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

    // ==========================================
    // PROJECTS
    // ==========================================

    @Override
    @Transactional
    public JobSeekerProjectDto addProject(JobSeekerProjectDto dto) {
        JobSeekerProfile profile = getLoggedInJobSeekerProfile();

        JobSeekerProject project = new JobSeekerProject();
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setTechStack(dto.getTechStack());
        project.setGithubUrl(dto.getGithubUrl());
        project.setLiveDemoUrl(dto.getLiveDemoUrl());
        project.setClientName(dto.getClientName());
        project.setRole(dto.getRole());
        project.setTeamSize(dto.getTeamSize());
        project.setProjectStatus(dto.getProjectStatus());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());
        project.setJobSeekerProfile(profile);

        JobSeekerProject saved = projectRepository.save(project);
        return mapToProjectDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobSeekerProjectDto> getMyProjects() {
        JobSeekerProfile profile = getLoggedInJobSeekerProfile();
        return projectRepository.findByJobSeekerProfile(profile)
                .stream()
                .map(this::mapToProjectDto)
                .toList();
    }

    @Override
    @Transactional
    public JobSeekerProjectDto updateProject(Long projectId, JobSeekerProjectDto dto) {
        JobSeekerProfile profile = getLoggedInJobSeekerProfile();
        JobSeekerProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getJobSeekerProfile().getJobSeekerProfileId().equals(profile.getJobSeekerProfileId())) {
            throw new RuntimeException("Unauthorized to modify this project");
        }

        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setTechStack(dto.getTechStack());
        project.setGithubUrl(dto.getGithubUrl());
        project.setLiveDemoUrl(dto.getLiveDemoUrl());
        project.setClientName(dto.getClientName());
        project.setRole(dto.getRole());
        project.setTeamSize(dto.getTeamSize());
        project.setProjectStatus(dto.getProjectStatus());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());

        return mapToProjectDto(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        JobSeekerProfile profile = getLoggedInJobSeekerProfile();
        JobSeekerProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getJobSeekerProfile().getJobSeekerProfileId().equals(profile.getJobSeekerProfileId())) {
            throw new RuntimeException("Unauthorized to delete this project");
        }

        projectRepository.delete(project);
    }

    // ==========================================
    // EXPERIENCES
    // ==========================================

    @Override
    @Transactional
    public JobSeekerExperienceDto addExperience(JobSeekerExperienceDto dto) {
        JobSeekerProfile profile = getLoggedInJobSeekerProfile();

        JobSeekerExperience exp = new JobSeekerExperience();
        exp.setCompanyName(dto.getCompanyName());
        exp.setDesignation(dto.getDesignation());
        exp.setLocation(dto.getLocation());
        exp.setStartDate(dto.getStartDate());
        exp.setEndDate(dto.getEndDate());
        exp.setIsCurrentJob(dto.getIsCurrentJob());
        exp.setEmploymentType(dto.getEmploymentType());
        exp.setDepartment(dto.getDepartment());
        exp.setCurrentCtc(dto.getCurrentCtc());
        exp.setNoticePeriod(dto.getNoticePeriod());
        exp.setResponsibilities(dto.getResponsibilities());
        exp.setTechStack(dto.getTechStack());
        exp.setJobSeekerProfile(profile);

        JobSeekerExperience saved = experienceRepository.save(exp);
        syncCurrentEmploymentToProfile(profile, saved);
        return mapToExperienceDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobSeekerExperienceDto> getMyExperiences() {
        JobSeekerProfile profile = getLoggedInJobSeekerProfile();
        return experienceRepository.findByJobSeekerProfile(profile)
                .stream()
                .map(this::mapToExperienceDto)
                .toList();
    }

    @Override
    @Transactional
    public JobSeekerExperienceDto updateExperience(Long experienceId, JobSeekerExperienceDto dto) {
        JobSeekerProfile profile = getLoggedInJobSeekerProfile();
        JobSeekerExperience exp = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        if (!exp.getJobSeekerProfile().getJobSeekerProfileId().equals(profile.getJobSeekerProfileId())) {
            throw new RuntimeException("Unauthorized to modify this experience");
        }

        exp.setCompanyName(dto.getCompanyName());
        exp.setDesignation(dto.getDesignation());
        exp.setLocation(dto.getLocation());
        exp.setStartDate(dto.getStartDate());
        exp.setEndDate(dto.getEndDate());
        exp.setIsCurrentJob(dto.getIsCurrentJob());
        exp.setEmploymentType(dto.getEmploymentType());
        exp.setDepartment(dto.getDepartment());
        exp.setCurrentCtc(dto.getCurrentCtc());
        exp.setNoticePeriod(dto.getNoticePeriod());
        exp.setResponsibilities(dto.getResponsibilities());
        exp.setTechStack(dto.getTechStack());

        JobSeekerExperience saved = experienceRepository.save(exp);
        syncCurrentEmploymentToProfile(profile, saved);
        return mapToExperienceDto(saved);
    }

    @Override
    @Transactional
    public void deleteExperience(Long experienceId) {
        JobSeekerProfile profile = getLoggedInJobSeekerProfile();
        JobSeekerExperience exp = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        if (!exp.getJobSeekerProfile().getJobSeekerProfileId().equals(profile.getJobSeekerProfileId())) {
            throw new RuntimeException("Unauthorized to delete this experience");
        }

        experienceRepository.delete(exp);
    }

    private void syncCurrentEmploymentToProfile(JobSeekerProfile profile, JobSeekerExperience exp) {
        if (Boolean.TRUE.equals(exp.getIsCurrentJob())) {
            profile.setCurrentCompany(exp.getCompanyName());
            profile.setCurrentDesignation(exp.getDesignation());
            if (exp.getCurrentCtc() != null) {
                profile.setCurrentSalary(exp.getCurrentCtc());
            }
            if (exp.getNoticePeriod() != null && !exp.getNoticePeriod().isBlank()) {
                profile.setNoticePeriod(exp.getNoticePeriod());
            }
            if (exp.getDepartment() != null && !exp.getDepartment().isBlank()) {
                profile.setDepartment(exp.getDepartment());
            }
            jobSeekerRepository.save(profile);
        }
    }

    private JobSeekerProjectDto mapToProjectDto(JobSeekerProject p) {
        JobSeekerProjectDto dto = new JobSeekerProjectDto();
        dto.setId(p.getId());
        dto.setTitle(p.getTitle());
        dto.setDescription(p.getDescription());
        dto.setTechStack(p.getTechStack());
        dto.setGithubUrl(p.getGithubUrl());
        dto.setLiveDemoUrl(p.getLiveDemoUrl());
        dto.setClientName(p.getClientName());
        dto.setRole(p.getRole());
        dto.setTeamSize(p.getTeamSize());
        dto.setProjectStatus(p.getProjectStatus());
        dto.setStartDate(p.getStartDate());
        dto.setEndDate(p.getEndDate());
        return dto;
    }

    private JobSeekerExperienceDto mapToExperienceDto(JobSeekerExperience e) {
        JobSeekerExperienceDto dto = new JobSeekerExperienceDto();
        dto.setId(e.getId());
        dto.setCompanyName(e.getCompanyName());
        dto.setDesignation(e.getDesignation());
        dto.setLocation(e.getLocation());
        dto.setStartDate(e.getStartDate());
        dto.setEndDate(e.getEndDate());
        dto.setIsCurrentJob(e.getIsCurrentJob());
        dto.setEmploymentType(e.getEmploymentType());
        dto.setDepartment(e.getDepartment());
        dto.setCurrentCtc(e.getCurrentCtc());
        dto.setNoticePeriod(e.getNoticePeriod());
        dto.setResponsibilities(e.getResponsibilities());
        dto.setTechStack(e.getTechStack());
        return dto;
    }
}
