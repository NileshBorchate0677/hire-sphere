package com.hiresphere.hiresphere.Auth.Service;




import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.hiresphere.hiresphere.Auth.Dto.RegisterUserRequestDto;
import com.hiresphere.hiresphere.Auth.Dto.UserDto;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Mapper.AuthMapper;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Exception.EmailAlreadyExistException;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SessionService sessionService;
	private final com.hiresphere.hiresphere.SavedJob.Repository.SavedJobRepository savedJobRepository;
	private final com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerRepository jobSeekerRepository;
	private final com.hiresphere.hiresphere.Application.Repository.ApplicationRepository applicationRepository;
	private final com.hiresphere.hiresphere.Recruiter.Repository.RecruiterRepository recruiterRepository;
	private final com.hiresphere.hiresphere.Job.Repository.JobRepository jobRepository;
	private final com.hiresphere.hiresphere.Auth.Repository.PasswordResetTokenRepository passwordResetTokenRepository;
	private final com.hiresphere.hiresphere.Notification.Repository.NotificationRepository notificationRepository;
	private final EmailService emailService;

	
	//this method is authenticate the User_name and password through userDetilsService interface
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository.findByEmail(username)
				.orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
		
	}
	
	
	
	
	// to find the by userId for Sessions refresh 
	
	public Users findByUsersId(Long UsersId)
	{
		return userRepository.findById(UsersId)
				.orElseThrow(() -> new BadCredentialsException("the userId is no found"));
	}




	
	// SignUp User API Logic(register the user in DB)
	public UserDto signUp(RegisterUserRequestDto requestDto) {
		
		Optional<Users> user = userRepository.findByEmail(requestDto.getEmail());
		
		if(user.isPresent())
		{
			throw new EmailAlreadyExistException("Email Already Exits. Please register with another email ");
		} 
		
		// encode the password
		Users newUser = AuthMapper.maptoUsers(requestDto);
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		
		
		//give Role for only User
		
		if(requestDto.getRole() == UserRoles.JOB_SEEKER){

		    newUser.setRole(UserRoles.JOB_SEEKER);

		}
		else if(requestDto.getRole() == UserRoles.RECRUITER){

		    newUser.setRole(UserRoles.RECRUITER);

		}
		else if(requestDto.getRole() == UserRoles.ADMIN){

		    newUser.setRole(UserRoles.ADMIN);
		}
		else{

		    throw new BadCredentialsException("Invalid Role");
		}
		
		Users userSaved= userRepository.save(newUser);
		
		return AuthMapper.maptoUserDto(userSaved);
		
	}


	// GET CURRENT LOGGED-IN USER
	public UserDto getCurrentUser() {

	    String email = SecurityContextHolder
	            .getContext()
	            .getAuthentication()
	            .getName();

	    Users user = userRepository
	            .findByEmail(email)
	            .orElseThrow(() ->
	                    new UsernameNotFoundException(
	                            "Logged-in user not found"
	                    )
	            );

	    return AuthMapper.maptoUserDto(user);
	}

	// UPDATE USER NAME
	@org.springframework.transaction.annotation.Transactional
	public UserDto updateName(com.hiresphere.hiresphere.Auth.Dto.UpdateNameRequestDto dto) {
	    String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    Users user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("Logged-in user not found"));

	    user.setName(dto.getName().trim());
	    Users updated = userRepository.save(user);
	    return AuthMapper.maptoUserDto(updated);
	}

	// GET ACTIVE LOGIN SESSIONS
	public java.util.List<com.hiresphere.hiresphere.Auth.Dto.UserSessionResponseDto> getMySessions() {
	    String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    Users user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("Logged-in user not found"));

	    return user.getUserId() != null 
	            ? userRepository.findById(user.getUserId())
	                .map(u -> sessionService.getUserSessions(u).stream().map(s -> {
	                    com.hiresphere.hiresphere.Auth.Dto.UserSessionResponseDto sdto = new com.hiresphere.hiresphere.Auth.Dto.UserSessionResponseDto();
	                    sdto.setId(s.getId());
	                    sdto.setCreatedAt(s.getLastCreatedAt());
	                    return sdto;
	                }).toList()).orElse(java.util.List.of())
	            : java.util.List.of();
	}

	// TERMINATE SPECIFIC SESSION
	@org.springframework.transaction.annotation.Transactional
	public void terminateSession(Long sessionId) {
	    String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    Users user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("Logged-in user not found"));

	    sessionService.deleteSessionById(user, sessionId);
	}

	// DELETE ACCOUNT COMPLETELY
	@org.springframework.transaction.annotation.Transactional
	public void deleteAccount(com.hiresphere.hiresphere.Auth.Dto.DeleteAccountRequestDto dto) {
	    String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    Users user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("Logged-in user not found"));

	    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
	        throw new RuntimeException("Incorrect password. Account deletion aborted.");
	    }

	    // 1. Delete all user notifications
	    notificationRepository.deleteByUser(user);

	    // 2. Delete all pending password reset tokens
	    passwordResetTokenRepository.deleteByUser(user);

	    // 3. Delete all user saved jobs
	    savedJobRepository.deleteByUser(user);

	    // 4. Delete all sessions
	    sessionService.logoutAllDevices(user);

	    // 5. Delete Job Seeker Profile and applications if exists
	    var jsOpt = jobSeekerRepository.findByUser(user);
	    if (jsOpt.isPresent()) {
	        var js = jsOpt.get();
	        applicationRepository.deleteByJobSeekerProfile(js);
	        jobSeekerRepository.delete(js);
	    }

	    // 6. Delete Recruiter Profile, Jobs and Applications if exists
	    var recOpt = recruiterRepository.findByUser(user);
	    if (recOpt.isPresent()) {
	        var rec = recOpt.get();
	        var jobs = jobRepository.findByRecruiterProfile(rec);
	        if (!jobs.isEmpty()) {
	            applicationRepository.deleteByJobIn(jobs);
	            savedJobRepository.deleteByJobIn(jobs);
	            jobRepository.deleteByRecruiterProfile(rec);
	        }
	        recruiterRepository.delete(rec);
	    }

	    // 7. Delete User record
	    userRepository.delete(user);
	}

	// 11) INITIATE FORGOT PASSWORD REQUEST
	@org.springframework.transaction.annotation.Transactional
	public com.hiresphere.hiresphere.Auth.Dto.ForgotPasswordResponseDto initiatePasswordReset(com.hiresphere.hiresphere.Auth.Dto.ForgotPasswordRequestDto dto) {
	    String email = dto.getEmail().trim().toLowerCase();
	    Optional<Users> userOpt = userRepository.findByEmail(email);

	    if (userOpt.isEmpty()) {
	        throw new com.hiresphere.hiresphere.Exception.ResourceNotFoundException(
	                "No registered account found with email: " + email + ". Please check your address or create a new account.");
	    }

	    Users user = userOpt.get();
	    // Clean up any existing pending tokens for this user
	    passwordResetTokenRepository.deleteByUser(user);

	    // Generate 6-digit numeric OTP (e.g. 482910)
	    int randomNum = 100000 + new java.security.SecureRandom().nextInt(900000);
	    String token = String.valueOf(randomNum);

	    com.hiresphere.hiresphere.Auth.Entity.PasswordResetToken resetToken = com.hiresphere.hiresphere.Auth.Entity.PasswordResetToken.builder()
	            .token(token)
	            .user(user)
	            .expiryDate(java.time.LocalDateTime.now().plusMinutes(30))
	            .used(false)
	            .createdAt(java.time.LocalDateTime.now())
	            .build();

	    passwordResetTokenRepository.save(resetToken);

	    // Send OTP email via SMTP (e.g. Gmail)
	    boolean emailSent = emailService.sendPasswordResetOtp(email, token, user.getName());

	    // OTP logged at DEBUG only — never visible in production log level INFO+
	    log.debug("[AUTH] Password Reset OTP generated for {}", email);

	    String responseMessage = emailSent
	            ? "6-digit verification code has been dispatched to your email (" + email + "). Valid for 30 minutes."
	            : "6-digit verification code generated for " + email + ". Valid for 30 minutes.";

	    // In production with email enabled, hide resetToken from JSON response for enterprise security.
	    // In offline development without SMTP, return token so local dev auto-fill still functions.
	    String returnedToken = emailSent ? null : token;

	    return com.hiresphere.hiresphere.Auth.Dto.ForgotPasswordResponseDto.builder()
	            .message(responseMessage)
	            .resetToken(returnedToken)
	            .build();
	}


	// 12) RESET PASSWORD WITH SECURITY TOKEN
	@org.springframework.transaction.annotation.Transactional
	public void resetPasswordWithToken(com.hiresphere.hiresphere.Auth.Dto.ResetPasswordRequestDto dto) {
	    String token = dto.getToken().trim().toUpperCase();
	    com.hiresphere.hiresphere.Auth.Entity.PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
	            .orElseThrow(() -> new com.hiresphere.hiresphere.Exception.BadRequestException("Invalid or expired password reset token."));

	    if (resetToken.isUsed()) {
	        throw new com.hiresphere.hiresphere.Exception.BadRequestException("This password reset token has already been used.");
	    }

	    if (resetToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
	        throw new com.hiresphere.hiresphere.Exception.BadRequestException("This password reset token has expired. Please request a new one.");
	    }

	    Users user = resetToken.getUser();
	    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
	    userRepository.save(user);

	    resetToken.setUsed(true);
	    passwordResetTokenRepository.save(resetToken);

	    // Invalidate existing sessions for security
	    sessionService.logoutAllDevices(user);
	}
}



