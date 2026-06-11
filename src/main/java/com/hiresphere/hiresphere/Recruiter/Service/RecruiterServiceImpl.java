package com.hiresphere.hiresphere.Recruiter.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Recruiter.DTO.GetRecruiterProfileResponseDto;
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
	
		
		// now map in the RecruiterProfile table recruiter Details 
        
        RecruiterProfile recruiterProfile = RecruiterMapper.mapToRecruiterProfile(
        		recruiterProfileRequestDto);
        
        // attach the login user becouse our Users user is not null
        
        recruiterProfile.setUser(user);
        
        // now saved the Recruiter profile 
        
        RecruiterProfile savedRecruiterProfile = recruiterRepository.save(recruiterProfile);
        
        //now return the response
		return RecruiterMapper.mapToRecuiterResponceDto(savedRecruiterProfile);
	}

	
	
	
	
	// To get the recruiter own profile
	@Override
	public GetRecruiterProfileResponseDto getRecruiterProfile() {
		
		// check user login 
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email=authentication.getName();
		
		//user is check 
		Users user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("user not found"));
		
		// from the above user find to check the user role is REcruiter or not 
		
		if(user.getRole() != UserRoles.RECRUITER){
			
			throw new RuntimeException("only Recruiter can create the Recruiter Profile");
			
		}
		
		// now find the profile in Db by using the find user
		
		RecruiterProfile getProfile =recruiterRepository.findByUser(user)
				.orElseThrow(() -> new RuntimeException("User is not found"));
		
		GetRecruiterProfileResponseDto profile =RecruiterMapper.mapToGetRecruiterProfileDetailsDto(getProfile);
		
		return profile;
	}



 

	@Override
	public RecruiterProfileResponseDto updateRecruiterProfile(RecruiterProfileRequestDto Dto) {
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
				
		RecruiterProfile recruiterProfile =
		        recruiterRepository
		                .findByUser(user)
		                .orElseThrow(() ->
		                        new RuntimeException(
		                                "Recruiter profile not found"));
		
		recruiterProfile.setCompanyName(
		        Dto.getCompanyName());

		recruiterProfile.setCompanyDescription(
		        Dto.getCompanyDescription());

		recruiterProfile.setWebsite(
		        Dto.getWebsite());

		recruiterProfile.setLocation(
		        Dto.getLocation());

		recruiterProfile.setIndustry(
		        Dto.getIndustry());

		recruiterProfile.setCompanySize(
		        Dto.getCompanySize());

		recruiterProfile.setCompanyEmail(
		        Dto.getCompanyEmail());

		recruiterProfile.setCompanyPhone(
		        Dto.getCompanyPhone());
		
		RecruiterProfile updatedProfile =
		        recruiterRepository.save(
		                recruiterProfile);
		
		return RecruiterMapper
		        .mapToRecuiterResponceDto(
		                updatedProfile);
			
			
	}




	// delete the user profile 

	@Override
	public void deleteRecruiterProfile()
	{
	    // Get Logged In User

	    Authentication authentication =
	            SecurityContextHolder
	                    .getContext()
	                    .getAuthentication();

	    String email = authentication.getName();

	    // Find User

	    Users user = userRepository
	            .findByEmail(email)
	            .orElseThrow(() ->
	                    new RuntimeException(
	                            "User not found"));

	    // Role Check

	    if(user.getRole() != UserRoles.RECRUITER)
	    {
	        throw new RuntimeException(
	                "Only recruiter can delete profile");
	    }

	    // Find Recruiter Profile

	    RecruiterProfile recruiterProfile =
	            recruiterRepository
	                    .findByUser(user)
	                    .orElseThrow(() ->
	                            new RuntimeException(
	                                    "Recruiter profile not found"));

	    // Delete Profile

	    recruiterRepository.delete(recruiterProfile);
	}

	
	 
	
	
	
	
}
