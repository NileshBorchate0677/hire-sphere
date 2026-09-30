package com.hiresphere.hiresphere.Auth.Dto;

import com.hiresphere.hiresphere.Auth.Enums.UserRoles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponceDto {

	private Long id; 
	
	private String accessToken; 
	
	private String refreshToken;
	
	private UserRoles role;
}
