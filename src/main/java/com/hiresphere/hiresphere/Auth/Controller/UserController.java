package com.hiresphere.hiresphere.Auth.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiresphere.hiresphere.Auth.Dto.ChangePasswordRequestDto;
import com.hiresphere.hiresphere.Auth.Dto.LoginUserRequestDto;
import com.hiresphere.hiresphere.Auth.Dto.RegisterUserRequestDto;
import com.hiresphere.hiresphere.Auth.Dto.UserDto;
import com.hiresphere.hiresphere.Auth.Dto.UserLoginResponceDto;
import com.hiresphere.hiresphere.Auth.Service.AuthService;
import com.hiresphere.hiresphere.Auth.Service.UserService;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth")
public class UserController {

	private final AuthService authService;
	
	private final UserService userService;
	
	
	
	@Value("${deploy.env}")
	private String deployEnv;
//	
	
	//1)  Registration API
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> userSignUp(@Valid @RequestBody RegisterUserRequestDto requestDto)
	{
		UserDto userDto = userService.signUp(requestDto);
		return ResponseEntity.ok(userDto);
	}

	
	
	//2) Login API for Job_Seeker and the Recruiter
	
	@PostMapping("/login")
	public ResponseEntity<UserLoginResponceDto> userSignIn(@Valid @RequestBody LoginUserRequestDto loginUserRequestDto,
			HttpServletResponse response)
	{
		UserLoginResponceDto responceDto =authService.userSignIn(loginUserRequestDto);
		
		Cookie cookie= new Cookie("refreshToken", responceDto.getRefreshToken());
		 cookie.setHttpOnly(true);
		 cookie.setSecure("production".equals(deployEnv));
		 cookie.setPath("/");                 // VERY IMPORTANT Available for whole website
		    //cookie.setMaxAge(7 * 24 * 60 * 60);  // 7 days

		    response.addCookie(cookie); 
		 
		return ResponseEntity.ok(responceDto); 
	} 
	
	
	
	
	
	
	
	
	
	
	
	//3) REST APi for the Refresh the AccessToken
	
	@PostMapping("/refresh")
	public ResponseEntity<UserLoginResponceDto> refresh(HttpServletRequest request)
	{
		if (request.getCookies() == null) {
	        throw new AuthenticationServiceException("No cookies found");
	    } 
		
		String refreshToken =Arrays.stream(request.getCookies())
				.filter(Cookie -> "refreshToken".equals(Cookie.getName()))
				.findFirst()
				.map(Cookie::getValue) 
				.orElseThrow( () -> new AuthenticationServiceException
						("refresh token inside the cookie is not found"
						));
		
		
	UserLoginResponceDto	 loginResponceDto=authService.refresh(refreshToken);
		
		return ResponseEntity.ok(loginResponceDto);
	}
	
	
	
	
	// 4)logout the current session of user
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request , HttpServletResponse response)
	{
	    authService.logout(request, response);
	     
	    return ResponseEntity.ok( "Logout Successfully");  
	}
	
	
	// 5) logout the user from All devices
	
	@PostMapping("/logoutAll")
	public ResponseEntity<String>logoutAllDevices(HttpServletResponse response)
	{
	    authService.logoutAllDevices( response);

	    return ResponseEntity.ok( "Logout From All Devices Successfully");
	}
	
	
	
	
	
	//6) Change the Password of User 
	
	@PutMapping("/change-password")
	public ResponseEntity<String>changePassword(  @Valid @RequestBody ChangePasswordRequestDto dto)
	{
	    authService.changePassword(dto);

	    return ResponseEntity.ok( "Password Changed Successfully");
	}
	
	
} 
