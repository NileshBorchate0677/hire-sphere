package com.hiresphere.hiresphere.Auth.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDto {

    @NotBlank(
            message = "Old Password Is Required")
    private String oldPassword;

    @NotBlank(
            message = "New Password Is Required")
    @Size(
            min = 8,
            message = "Password Must Be At Least 8 Characters")
    private String newPassword;
}