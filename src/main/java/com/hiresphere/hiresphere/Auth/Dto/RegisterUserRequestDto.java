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

	@NotBlank(message = "Name is required")
	private String name;
	
	@Email(message = "Invalid Email Format")
	@NotBlank(message = "Email is required")
	private String email;
	
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;
	
	@NotNull(message = "The role is required")
	private UserRoles role;

	public void setFullName(String fullName) {
		if (this.name == null || this.name.isBlank()) {
			this.name = fullName;
		}
	}

	public String getFullName() {
		return this.name;
	}
	
	
	
}
