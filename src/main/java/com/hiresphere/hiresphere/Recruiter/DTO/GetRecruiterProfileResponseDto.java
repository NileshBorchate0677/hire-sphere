package com.hiresphere.hiresphere.Recruiter.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetRecruiterProfileResponseDto {

    private Long id;

    private String recruiterName;

    private String companyName;

    private String companyDescription;

    private String website;

    private String location;

    private String industry;

    private Integer companySize;

    private String companyEmail;

    private String companyPhone;

    private LocalDateTime createdAt;
}
