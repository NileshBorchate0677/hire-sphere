package com.hiresphere.hiresphere.Auth.Dto;

import com.hiresphere.hiresphere.Auth.Enums.UserRoles;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequestDto {

	@NotBlank(message = " name is required")
	private String name;
	
	@Email(message = "Invalid Email Format")
	@NotBlank(message = "email is required")
	private String email;
	
	@NotBlank(message = "password is required")
	@Size(min =8, message = "Password must be At least 8 chacters ")
	private String password;
	
	@NotNull(message = "the role is required")
	private UserRoles role;
	
	
	
}
