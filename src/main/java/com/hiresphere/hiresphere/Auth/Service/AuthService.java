package com.hiresphere.hiresphere.Auth.Service;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Auth.Dto.ChangePasswordRequestDto;
import com.hiresphere.hiresphere.Auth.Dto.LoginUserRequestDto;
import com.hiresphere.hiresphere.Auth.Dto.UserLoginResponceDto;
import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Auth.Security.JwtServiceProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final AuthenticationManager authenticationManager;
	
	private final UserService userService;
	
	private final JwtServiceProvider jwtServiceProvider;
	
	private final SessionService sessionService;
	
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	
	
	
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







	public void logout( HttpServletRequest request, HttpServletResponse response)
	{
	    Cookie[] cookies = request.getCookies();
	    

	    if(cookies == null)
	    {
	        throw new RuntimeException("No Cookies Found");
	    }
	    

	    String refreshToken = null;

	    for(Cookie cookie : cookies)
	    {
	        if(cookie.getName().equals("refreshToken"))
	        {
	            refreshToken =cookie.getValue();

	            break;
	        }
	    }

	    if(refreshToken == null)
	    {
	        throw new RuntimeException( "Refresh Token Not Found");
	    }

	    sessionService.logout(refreshToken);

	    Cookie deleteCookie = new Cookie("refreshToken",null);

	    deleteCookie.setPath("/");

	    deleteCookie.setMaxAge(0);

	    response.addCookie( deleteCookie);
	}







	public void logoutAllDevices(HttpServletResponse response) {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null
	            || !authentication.isAuthenticated()
	            || authentication instanceof AnonymousAuthenticationToken) {
	        throw new AuthenticationServiceException("Access token is invalid or expired");
	    }

	    Users user = (Users) authentication.getPrincipal();

	    sessionService.logoutAllDevices(user);

	    Cookie deleteCookie = new Cookie("refreshToken", null);
	    deleteCookie.setPath("/");
	    deleteCookie.setMaxAge(0);
	    response.addCookie(deleteCookie);
	}






	public void changePassword(ChangePasswordRequestDto dto) {

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null
	            || !authentication.isAuthenticated()
	            || authentication instanceof AnonymousAuthenticationToken) {
	        throw new AuthenticationServiceException("Access token is invalid or expired");
	    }

	    Users user = (Users) authentication.getPrincipal();

	    if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
	        throw new RuntimeException("Old Password Is Incorrect");
	    }

	    if (dto.getOldPassword().equals(dto.getNewPassword())) {
	        throw new RuntimeException("New Password Cannot Be Same As Old Password");
	    }

	    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

	    userRepository.save(user);

	    sessionService.logoutAllDevices(user);
	}
	
	 
	

} 
