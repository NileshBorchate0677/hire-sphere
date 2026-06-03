package com.hiresphere.hiresphere.Auth.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Auth.Dto.LoginUserRequestDto;
import com.hiresphere.hiresphere.Auth.Dto.UserLoginResponceDto;
import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Security.JwtServiceProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final AuthenticationManager authenticationManager;
	
	private final UserService userService;
	
	private final JwtServiceProvider jwtServiceProvider;
	
	private final SessionService sessionService;

	
	
	
	// logic for the user to login 
	
	public  UserLoginResponceDto userSignIn(LoginUserRequestDto loginUserRequestDto) {
		
		// this compares the Credentials to the user data base 
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUserRequestDto.getEmail(), 
						loginUserRequestDto.getPassword()));
		
		Users user =(Users) authentication.getPrincipal();
		
		String accessToken = jwtServiceProvider.genrateAccsessToken(user);
		
		String refreshToken = jwtServiceProvider.genrateRefreshToken(user);
		
		// give the new Session to the user using the refreshToken And the user id
		 
		sessionService.genrateNewSession(user, refreshToken);
		
		
		return new UserLoginResponceDto(user.getUserId(), accessToken, refreshToken) ;
	}



	
	
	
	
	
	
	
	
	
	// the Api logic to refresh the Access Token by the refresh Token 

	public UserLoginResponceDto refresh(String refreshToken) {
		
		Long userId= jwtServiceProvider.getUserIdfromToken(refreshToken);
		
		sessionService.validateRefreshToken(refreshToken);
		
		Users user = userService.findByUsersId(userId);
		
		String accsessToken =jwtServiceProvider.genrateAccsessToken(user);
		
		
		return new UserLoginResponceDto(user.getUserId(), accsessToken, refreshToken);
	}
	

} 
