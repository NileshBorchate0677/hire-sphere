package com.hiresphere.hiresphere.Auth.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequestDto {
	

	@NotBlank(message = "Email is required") 
	@Email(message = "Invalid Email format")
	private String email;
	
	@NotBlank(message = "Passwords is required")
	private String password;
	
	


}
