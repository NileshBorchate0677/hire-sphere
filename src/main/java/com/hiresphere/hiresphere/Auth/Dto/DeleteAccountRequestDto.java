package com.hiresphere.hiresphere.Auth.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteAccountRequestDto {

    @NotBlank(message = "Password is required to confirm account deletion")
    private String password;
}
