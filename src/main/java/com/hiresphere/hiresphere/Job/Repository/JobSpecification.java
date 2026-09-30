package com.hiresphere.hiresphere.Job.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.hiresphere.hiresphere.Job.Entity.Job;
import com.hiresphere.hiresphere.Job.Enums.JobStatus;
import com.hiresphere.hiresphere.Job.Enums.JobType;
import com.hiresphere.hiresphere.Job.Enums.WorkplaceType;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class JobSpecification {

    public static Specification<Job> filterJobs(
            String keyword,
            String location,
            String jobType,
            String workplaceType,
            Double minSalary,
            Integer experience) {
        return filterJobs(keyword, location, jobType, workplaceType, minSalary, experience, null);
    }

    public static Specification<Job> filterJobs(
            String keyword,
            String location,
            String jobType,
            String workplaceType,
            Double minSalary,
            Integer experience,
            Integer postedWithinDays) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Eagerly fetch recruiterProfile on data queries to prevent N+1 and LazyInitializationException.
            // Guard: skip fetch on the COUNT query that Spring Data issues for pagination.
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("recruiterProfile", JoinType.LEFT);
                query.distinct(true);
            }

            // Reuse or create join to recruiterProfile for filtering
            Join<Job, RecruiterProfile> recruiterJoin = null;
            for (Join<Job, ?> j : root.getJoins()) {
                if ("recruiterProfile".equals(j.getAttribute().getName())) {
                    @SuppressWarnings("unchecked")
                    Join<Job, RecruiterProfile> casted = (Join<Job, RecruiterProfile>) j;
                    recruiterJoin = casted;
                    break;
                }
            }
            if (recruiterJoin == null) {
                recruiterJoin = root.join("recruiterProfile", JoinType.LEFT);
            }

            // Public job search only returns OPEN jobs
            predicates.add(cb.equal(root.get("status"), JobStatus.OPEN));

            // Intelligent Tokenized Multi-Keyword Search (Naukri.com style)
            if (keyword != null && !keyword.trim().isEmpty()) {
                // Split keyword on spaces, commas, plus signs, slashes
                String[] tokens = keyword.replaceAll("[,;+/]+", " ").trim().split("\\s+");
                List<Predicate> tokenPredicates = new ArrayList<>();

                for (String rawToken : tokens) {
                    String token = rawToken.trim().toLowerCase();
                    if (token.isEmpty()) continue;

                    String pattern = "%" + token + "%";
                    Predicate titleMatch   = cb.like(cb.lower(root.get("title")), pattern);
                    Predicate descMatch    = cb.like(cb.lower(root.get("description")), pattern);
                    Predicate skillsMatch  = cb.like(cb.lower(root.get("requiredSkills")), pattern);
                    Predicate companyMatch = cb.like(cb.lower(recruiterJoin.get("companyName")), pattern);
                    Predicate locationMatch = cb.like(cb.lower(root.get("location")), pattern);

                    // Token must match at least one attribute
                    tokenPredicates.add(cb.or(titleMatch, descMatch, skillsMatch, companyMatch, locationMatch));
                }

                if (!tokenPredicates.isEmpty()) {
                    predicates.add(cb.and(tokenPredicates.toArray(new Predicate[0])));
                }
            }

            // Dedicated Location filter
            if (location != null && !location.trim().isEmpty()) {
                String locPattern = "%" + location.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("location")), locPattern));
            }

            // JobType filter (e.g. FULL_TIME, PART_TIME, INTERNSHIP)
            if (jobType != null && !jobType.trim().isEmpty()) {
                try {
                    String normalized = jobType.trim().toUpperCase().replace("-", "_").replace(" ", "_");
                    JobType typeEnum = JobType.valueOf(normalized);
                    predicates.add(cb.equal(root.get("jobType"), typeEnum));
                } catch (IllegalArgumentException ignored) {
                }
            }

            // WorkplaceType filter (REMOTE, HYBRID, ON_SITE)
            if (workplaceType != null && !workplaceType.trim().isEmpty()) {
                try {
                    String normalized = workplaceType.trim().toUpperCase().replace("-", "_").replace(" ", "_");
                    if ("ONSITE".equals(normalized)) normalized = "ON_SITE";
                    WorkplaceType wpEnum = WorkplaceType.valueOf(normalized);
                    predicates.add(cb.equal(root.get("workplaceType"), wpEnum));
                } catch (IllegalArgumentException ignored) {
                }
            }

            // Minimum salary filter (LPA)
            if (minSalary != null && minSalary > 0) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maxSalary"), minSalary));
            }

            // Experience filter (Max years required <= candidate's experience or required range)
            if (experience != null && experience >= 0) {
                predicates.add(cb.lessThanOrEqualTo(root.get("experienceRequired"), experience));
            }

            // Freshness filter (posted within N days)
            if (postedWithinDays != null && postedWithinDays > 0) {
                LocalDateTime cutoff = LocalDateTime.now().minusDays(postedWithinDays);
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), cutoff));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
