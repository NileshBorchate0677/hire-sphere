package com.hiresphere.hiresphere.Auth.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.GetMapping;
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
		 cookie.setPath("/");
		 cookie.setMaxAge(30 * 24 * 60 * 60); // 30 days (matches refresh token TTL)
		 response.addCookie(cookie);

		 // SameSite=Strict via header (Java Cookie API doesn't support SameSite directly)
		 if ("production".equals(deployEnv)) {
		     response.addHeader("Set-Cookie",
		         "refreshToken=" + responceDto.getRefreshToken()
		         + "; Path=/; HttpOnly; Secure; SameSite=Strict; Max-Age=" + (30 * 24 * 60 * 60));
		 }
		 
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
	
	
	// get profile name
	@GetMapping("/me")
	public ResponseEntity<UserDto> getCurrentUser() {

	    return ResponseEntity.ok(
	            userService.getCurrentUser()
	    );
	}

	// 7) Update Display Name
	@PutMapping("/update-name")
	public ResponseEntity<UserDto> updateName(@Valid @RequestBody com.hiresphere.hiresphere.Auth.Dto.UpdateNameRequestDto dto) {
	    return ResponseEntity.ok(userService.updateName(dto));
	}

	// 8) Get Active Sessions
	@GetMapping("/sessions")
	public ResponseEntity<java.util.List<com.hiresphere.hiresphere.Auth.Dto.UserSessionResponseDto>> getMySessions() {
	    return ResponseEntity.ok(userService.getMySessions());
	}

	// 9) Terminate Specific Session
	@org.springframework.web.bind.annotation.DeleteMapping("/sessions/{sessionId}")
	public ResponseEntity<java.util.Map<String, String>> terminateSession(@org.springframework.web.bind.annotation.PathVariable Long sessionId) {
	    userService.terminateSession(sessionId);
	    return ResponseEntity.ok(java.util.Map.of("message", "Session terminated successfully"));
	}

	// 10) Delete Account Permanently
	@org.springframework.web.bind.annotation.DeleteMapping("/delete-account")
	public ResponseEntity<java.util.Map<String, String>> deleteAccount(@Valid @RequestBody com.hiresphere.hiresphere.Auth.Dto.DeleteAccountRequestDto dto) {
	    userService.deleteAccount(dto);
	    return ResponseEntity.ok(java.util.Map.of("message", "Account and all associated records permanently deleted"));
	}

	// 11) Forgot Password Request
	@PostMapping("/forgot-password")
	public ResponseEntity<com.hiresphere.hiresphere.Auth.Dto.ForgotPasswordResponseDto> forgotPassword(
	        @Valid @RequestBody com.hiresphere.hiresphere.Auth.Dto.ForgotPasswordRequestDto dto) {
	    return ResponseEntity.ok(userService.initiatePasswordReset(dto));
	}

	// 12) Reset Password with Token
	@PostMapping("/reset-password")
	public ResponseEntity<java.util.Map<String, String>> resetPassword(
	        @Valid @RequestBody com.hiresphere.hiresphere.Auth.Dto.ResetPasswordRequestDto dto) {
	    userService.resetPasswordWithToken(dto);
	    return ResponseEntity.ok(java.util.Map.of("message", "Password has been successfully reset. Please sign in with your new password."));
	}
}

