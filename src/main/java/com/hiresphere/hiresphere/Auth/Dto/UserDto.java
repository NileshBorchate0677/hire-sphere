package com.hiresphere.hiresphere.Auth.Dto;

import com.hiresphere.hiresphere.Auth.Enums.UserRoles;

import lombok.Data;

@Data
public class UserDto {

	private Long id;
	
	private String name;
	
	private String email;
	
	private UserRoles role;

	
} 