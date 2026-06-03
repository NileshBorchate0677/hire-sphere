package com.hiresphere.hiresphere.Auth.Service;




import java.util.Optional;


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


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	
	//this method is authenticate the User_name and password through userDetilsService interface
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository.findByEmail(username)
				.orElseThrow(() -> new BadCredentialsException("the User Name is not found" + username));
		
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
			throw new BadCredentialsException("The Email is Already Exist"
					+ requestDto.getEmail());
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


     
	
	
		 
}


