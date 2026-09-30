package com.hiresphere.hiresphere.JobSeeker.Repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.hiresphere.hiresphere.JobSeeker.Entity.JobSeekerProfile;

import jakarta.persistence.criteria.Predicate;

public class JobSeekerSpecification {

    public static Specification<JobSeekerProfile> searchCandidates(
            String skill,
            String location,
            Integer minExp,
            Integer maxExp,
            String company,
            String designation,
            String noticePeriod,
            String workMode,
            Double maxExpectedSalary
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (skill != null && !skill.trim().isEmpty()) {
                String pattern = "%" + skill.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("skills")), pattern),
                        cb.like(cb.lower(root.get("headline")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("summary"), "")), pattern)
                ));
            }

            if (location != null && !location.trim().isEmpty()) {
                String pattern = "%" + location.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("location")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("preferredLocation"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("hometown"), "")), pattern)
                ));
            }

            if (minExp != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("experience"), minExp));
            }

            if (maxExp != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("experience"), maxExp));
            }

            if (company != null && !company.trim().isEmpty()) {
                String pattern = "%" + company.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(cb.coalesce(root.get("currentCompany"), "")), pattern));
            }

            if (designation != null && !designation.trim().isEmpty()) {
                String pattern = "%" + designation.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(cb.coalesce(root.get("currentDesignation"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("roleCategory"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("department"), "")), pattern)
                ));
            }

            if (noticePeriod != null && !noticePeriod.trim().isEmpty()) {
                String pattern = "%" + noticePeriod.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(cb.coalesce(root.get("noticePeriod"), "")), pattern));
            }

            if (workMode != null && !workMode.trim().isEmpty()) {
                String pattern = "%" + workMode.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(cb.coalesce(root.get("preferredWorkMode"), "")), pattern));
            }

            if (maxExpectedSalary != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("expectedSalary"), maxExpectedSalary));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
