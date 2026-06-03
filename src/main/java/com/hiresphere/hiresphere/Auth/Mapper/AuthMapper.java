package com.hiresphere.hiresphere.Auth.Mapper;

import com.hiresphere.hiresphere.Auth.Dto.RegisterUserRequestDto;
import com.hiresphere.hiresphere.Auth.Dto.UserDto;
import com.hiresphere.hiresphere.Auth.Entity.Users;


public class AuthMapper {
	
	public static Users maptoUsers(RegisterUserRequestDto Dto)
	{
		Users user = new Users();
		
		user.setName(Dto.getName());
		user.setEmail(Dto.getEmail());
		user.setPassword(Dto.getPassword());
		user.setRole(Dto.getRole());
		
		return user;
	}
	
	
	public static UserDto maptoUserDto(Users user)
	{
		UserDto Dto = new UserDto(); 
		
		Dto.setId(user.getUserId());
		Dto.setName(user.getName());
		Dto.setEmail(user.getEmail());
		Dto.setRole(user.getRole());
		
		
		return Dto;
	}
	
}
