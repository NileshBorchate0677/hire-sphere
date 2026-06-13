package com.hiresphere.hiresphere.Application.DTO;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyJobRequestDto {

    @Size(
            max = 1000,
            message = "Cover letter cannot exceed 1000 characters"
    )
    private String coverLetter;
}