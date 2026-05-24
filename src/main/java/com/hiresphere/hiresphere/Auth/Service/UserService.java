package com.hiresphere.hiresphere.Auth.Service;

import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Auth.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;

}
