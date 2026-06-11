package com.hiresphere.hiresphere.Recruiter.Mapper;

import com.hiresphere.hiresphere.Recruiter.DTO.GetRecruiterProfileResponseDto;
import com.hiresphere.hiresphere.Recruiter.DTO.RecruiterProfileRequestDto;
import com.hiresphere.hiresphere.Recruiter.DTO.RecruiterProfileResponseDto;
import com.hiresphere.hiresphere.Recruiter.Entity.RecruiterProfile;

public class RecruiterMapper {
	
	public static RecruiterProfile mapToRecruiterProfile(RecruiterProfileRequestDto Dto)
	{
		RecruiterProfile rp = new RecruiterProfile();
		
		rp.setCompanyName(Dto.getCompanyName());
		rp.setCompanyDescription(Dto.getCompanyDescription());
		rp.setWebsite(Dto.getWebsite());
		rp.setLocation(Dto.getLocation());
		rp.setIndustry(Dto.getIndustry());
		rp.setCompanySize(Dto.getCompanySize());
		rp.setCompanyEmail(Dto.getCompanyEmail());
		rp.setCompanyPhone(Dto.getCompanyPhone()); 
		
		return rp;
	}

	public static RecruiterProfileResponseDto mapToRecuiterResponceDto(RecruiterProfile rp)
	{
		RecruiterProfileResponseDto Dto =new RecruiterProfileResponseDto();
		
		Dto.setId(rp.getRecruiter_Profile_id());
		Dto.setCompanyName(rp.getCompanyName());
		Dto.setCompanyDescription(rp.getCompanyDescription());
		Dto.setWebsite(rp.getWebsite());
		Dto.setLocation(rp.getLocation());
		Dto.setIndustry(rp.getIndustry());
		Dto.setCompanySize(rp.getCompanySize());
		Dto.setCompanyEmail(rp.getCompanyEmail());
		Dto.setCompanyPhone(rp.getCompanyPhone());
		
		Dto.setCreatedAt(rp.getCreatedAt());
		
		return Dto;
	}
	
	
	public static GetRecruiterProfileResponseDto
	mapToGetRecruiterProfileDetailsDto(
	        RecruiterProfile profile)
	{
		GetRecruiterProfileResponseDto dto = new GetRecruiterProfileResponseDto();

	    dto.setId(profile.getRecruiter_Profile_id());

	    dto.setRecruiterName(
	            profile.getUser().getName());

	    dto.setCompanyName(
	            profile.getCompanyName());

	    dto.setCompanyDescription(
	            profile.getCompanyDescription());

	    dto.setWebsite(
	            profile.getWebsite());

	    dto.setLocation(
	            profile.getLocation());

	    dto.setIndustry(
	            profile.getIndustry());

	    dto.setCompanySize(
	            profile.getCompanySize());

	    dto.setCompanyEmail(
	            profile.getCompanyEmail());

	    dto.setCompanyPhone(
	            profile.getCompanyPhone());

	    dto.setCreatedAt(
	            profile.getCreatedAt());

	    return dto;
	}
	
	 
	
}
