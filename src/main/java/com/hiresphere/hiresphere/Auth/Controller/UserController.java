package com.hiresphere.hiresphere.Auth.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiresphere.hiresphere.Auth.Dto.RegisterUserRequestDto;
import com.hiresphere.hiresphere.Auth.Dto.UserDto;
import com.hiresphere.hiresphere.Auth.Service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

	private final UserService userService;
	
	
	//1)  Registration API
	
	@PostMapping("/signUp")
	public ResponseEntity<UserDto> userSignUp(@RequestBody RegisterUserRequestDto requestDto)
	{
		UserDto userDto = userService.signUp(requestDto);
		return ResponseEntity.ok(userDto);
	}

	
}
