package com.hiresphere.hiresphere.SavedJob.Service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Mapper.JobMapper;
import com.hiresphere.hiresphere.Job.Repository.JobRepository;
import com.hiresphere.hiresphere.SavedJob.Dto.SavedJobResponseDto;
import com.hiresphere.hiresphere.SavedJob.Entity.SavedJob;
import com.hiresphere.hiresphere.SavedJob.Repository.SavedJobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final SavedJobRepository savedJobRepository;

    private Users getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public SavedJobResponseDto saveJob(Long jobId) {
        Users user = getLoggedInUser();
        if (user.getRole() != UserRoles.JOB_SEEKER) {
            throw new RuntimeException("Only Job Seekers can save jobs");
        }

        Job job = jobRepository.findByIdWithRecruiter(jobId)
                .orElseGet(() -> jobRepository.findById(jobId)
                        .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId)));

        if (savedJobRepository.existsByUserAndJob(user, job)) {
            SavedJob existing = savedJobRepository.findByUserAndJob(user, job).get();
            return mapToDto(existing);
        }

        SavedJob savedJob = new SavedJob();
        savedJob.setUser(user);
        savedJob.setJob(job);
        SavedJob persisted = savedJobRepository.save(savedJob);

        return mapToDto(persisted);
    }

    @Override
    @Transactional
    public void removeSavedJob(Long jobId) {
        Users user = getLoggedInUser();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        savedJobRepository.deleteByUserAndJob(user, job);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavedJobResponseDto> getMySavedJobs() {
        Users user = getLoggedInUser();
        List<SavedJob> list = savedJobRepository.findByUserWithJobs(user);
        return list.stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isJobSaved(Long jobId) {
        Users user = getLoggedInUser();
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) return false;
        return savedJobRepository.existsByUserAndJob(user, job);
    }

    private SavedJobResponseDto mapToDto(SavedJob savedJob) {
        SavedJobResponseDto dto = new SavedJobResponseDto();
        dto.setId(savedJob.getId());
        dto.setJobId(savedJob.getJob().getId());
        dto.setJob(JobMapper.mapToJobResponseDto(savedJob.getJob()));
        dto.setSavedAt(savedJob.getSavedAt());
        return dto;
    }
}
