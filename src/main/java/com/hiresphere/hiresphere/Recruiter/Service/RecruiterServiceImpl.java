package com.hiresphere.hiresphere.Recruiter.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.hiresphere.hiresphere.Exception.RecruiterProfileNotFoundException;
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

	
	// Common Methods For the Login the Recruiter
	
	private Users getLoggedInRecruiter()
	{
	    Authentication authentication =
	            SecurityContextHolder
	                    .getContext()
	                    .getAuthentication();

	    String email = authentication.getName();

	    Users user = userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new RuntimeException("User not found"));

	    if(user.getRole() != UserRoles.RECRUITER)
	    {
	        throw new RuntimeException(
	                "Only recruiter can access this resource");
	    }

	    return user;
	}
	
	
	//for recruiter found or not 
	
	private RecruiterProfile getRecruiterProfileEntity()
	{
	    Users user = getLoggedInRecruiter();

	    return recruiterRepository.findByUser(user)
	            .orElseThrow(() ->
	                    new RecruiterProfileNotFoundException(
	                            "Recruiter profile not found"));
	}



	// this method for the update the recruiter 
	private void updateRecruiterProfileFields( RecruiterProfile profile, RecruiterProfileRequestDto dto)
	{
	    profile.setCompanyName(dto.getCompanyName());
	    profile.setCompanyDescription(dto.getCompanyDescription());
	    profile.setWebsite(dto.getWebsite());
	    profile.setLocation(dto.getLocation());
	    profile.setIndustry(dto.getIndustry());
	    profile.setCompanySize(dto.getCompanySize());
	    profile.setCompanyEmail(dto.getCompanyEmail());
	    profile.setCompanyPhone(dto.getCompanyPhone());
	}
	
	
	
	
	
	
	
	
	
	
	
	// 1)the logic for creating the profile of the Recruiter
	
	@Override
	public RecruiterProfileResponseDto createRecuiterprofile(
		 	RecruiterProfileRequestDto recruiterProfileRequestDto) {
		
		Users user = getLoggedInRecruiter();

	    if(recruiterRepository.existsByUser(user))
	    {
	        throw new RuntimeException(
	                "Recruiter profile already exists");
	    }

	    RecruiterProfile recruiterProfile =
	            RecruiterMapper.mapToRecruiterProfile(recruiterProfileRequestDto);

	    recruiterProfile.setUser(user);

	    RecruiterProfile savedProfile =
	            recruiterRepository.save(recruiterProfile);

	    return RecruiterMapper
	            .mapToRecuiterResponceDto(savedProfile);
	}

	
	
	
	
	// To get the recruiter own profile
	@Override
	public GetRecruiterProfileResponseDto getRecruiterProfile()
	{
	    RecruiterProfile profile =
	            getRecruiterProfileEntity();

	    return RecruiterMapper
	            .mapToGetRecruiterProfileDetailsDto(profile);
	}



	

	
	//3) Update the Recruiter Profile

	@Override
	public RecruiterProfileResponseDto updateRecruiterProfile(
	        RecruiterProfileRequestDto dto)
	{
	    RecruiterProfile recruiterProfile =
	            getRecruiterProfileEntity();

	    updateRecruiterProfileFields(
	            recruiterProfile,
	            dto);

	    RecruiterProfile updatedProfile =
	            recruiterRepository.save(
	                    recruiterProfile);

	    return RecruiterMapper
	            .mapToRecuiterResponceDto(
	                    updatedProfile);
	}




	//4) delete the user profile 

	@Override
	public void deleteRecruiterProfile()
	{
	    RecruiterProfile recruiterProfile =
	            getRecruiterProfileEntity();

	    recruiterRepository.delete(
	            recruiterProfile);
	}
	
	 
	
	
	
	
}
