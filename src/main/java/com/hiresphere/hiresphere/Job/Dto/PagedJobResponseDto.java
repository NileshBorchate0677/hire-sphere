package com.hiresphere.hiresphere.Job.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Generic paged response wrapper for job search results.
 * Carries pagination metadata alongside the content list.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedJobResponseDto {

    private List<JobResponseDto> content;

    /** 0-based current page number */
    private int page;

    /** Number of items requested per page */
    private int size;

    /** Total number of items matching the filter */
    private long totalElements;

    /** Total number of pages */
    private int totalPages;

    /** Whether this is the first page */
    private boolean first;

    /** Whether this is the last page */
    private boolean last;
}
