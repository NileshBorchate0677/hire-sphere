package com.hiresphere.hiresphere.JobSeeker.Mapper;

import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerEducationRequestDto;
import com.hiresphere.hiresphere.JobSeeker.Dto.JobSeekerEducationResponseDto;
import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerEducation;

public class JobSeekerEducationMapper {


    public static JobSeekerEducation
    mapToEntity(JobSeekerEducationRequestDto dto) {

        JobSeekerEducation education =
                new JobSeekerEducation();

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

        return education;
    }


    public static JobSeekerEducationResponseDto
    mapToResponseDto(JobSeekerEducation education) {

        JobSeekerEducationResponseDto dto =
                new JobSeekerEducationResponseDto();

        dto.setEducationId(
                education.getEducationId());

        dto.setEducationLevel(
                education.getEducationLevel());

        dto.setDegree(
                education.getDegree());

        dto.setInstitution(
                education.getInstitution());

        dto.setUniversity(
                education.getUniversity());

        dto.setPassingYear(
                education.getPassingYear());

        dto.setScore(
                education.getScore());

        dto.setScoreType(
                education.getScoreType());

        return dto;
    }
}