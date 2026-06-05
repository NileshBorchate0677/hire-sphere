package com.hiresphere.hiresphere.Recruiter.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Recruiter.DTO.RecruiterProfileRequestDto;
import com.hiresphere.hiresphere.Recruiter.DTO.RecruiterProfileResponseDto;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;
import com.hiresphere.hiresphere.Recruiter.Mapper.RecruiterMapper;
import com.hiresphere.hiresphere.Recruiter.Repository.RecruiterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImpl  implements RecruiterService{
	
	private final UserRepository userRepository;
	
	private final RecruiterRepository recruiterRepository;

	// 1)the logic for creating the profile of the Recruiter
	
	@Override
	public RecruiterProfileResponseDto createRecuiterprofile(
			RecruiterProfileRequestDto recruiterProfileRequestDto) {
		
		// get login user email from JWT filter saved SecurityContextHolder
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String email = authentication.getName();
		
		// from above user email(user name) to find the user in Users table
		
		Users user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("user not found"));
		
		// from the above user find to check the user role is REcruiter or not 
		
		if(user.getRole() != UserRoles.RECRUITER)
		{
			throw new RuntimeException("only Recruiter can create the Recruiter Profile");
		}
		
		
		// now check the recruiter is already exist or not means one Recruiter one profile
		
		if(recruiterRepository.existsByUser(user))
		{
            throw new RuntimeException(
                    "Recruiter profile already exists");
        }

		
		// company email is also a unique in data base no 2 companies have same email
        if (recruiterRepository.existsByCompanyEmail(recruiterProfileRequestDto.getCompanyEmail())) {
                throw new RuntimeException("Company email already exists");
        }

        
        // company phone number is also a unique in data base no 2 companies have same phone number
        if (recruiterRepository.existsByCompanyPhone(recruiterProfileRequestDto.getCompanyPhone())) {
        			throw new RuntimeException( "Company phone already exists");
        }

		
		
		// now map in the RecruiterProfile table recruiter Details 
        
        RecruiterProfile recruiterProfile = RecruiterMapper.mapToRecruiterProfile(
        		recruiterProfileRequestDto);
        
        // attach the login user beacouse our Users user is not null
        
        recruiterProfile.setUser(user);
        
        // now saved the Recruiter profile 
        
        RecruiterProfile savedRecruiterProfile = recruiterRepository.save(recruiterProfile);
        
        //now return the response
		return RecruiterMapper.mapToRecuiterResponceDto(savedRecruiterProfile);
	}

	
	
	
	
	
	
}
