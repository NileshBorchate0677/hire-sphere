package com.hiresphere.hiresphere.Recruiter.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecruiterProfileRequestDto {

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 200)
    private String companyName;

    @NotBlank(message = "Company description is required")
    @Size(min = 20, max = 1000)
    private String companyDescription;

    @NotBlank(message = "Location is required")
    @Size(max = 100)
    private String location;

    @NotBlank(message = "Industry is required")
    @Size(max = 100)
    private String industry;

    @NotBlank(message = "Company email is required")
    @Email(message = "Invalid email format")
    private String companyEmail;

    @NotBlank(message = "Company phone is required")
    @Pattern( regexp = "^[6-9]\\d{9}$",  message = "Invalid phone number")
    private String companyPhone;

    @NotBlank(message = "Company website is required")
    private String website;

    @NotNull(message = "Company size is required")
    @Positive(message = "Company size must be greater than 0")
    private Integer companySize;
}

