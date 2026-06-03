package com.hiresphere.hiresphere.Auth.Dto;

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
	
	private String accsesToken; 
	
	private String refreshToken; 
}
