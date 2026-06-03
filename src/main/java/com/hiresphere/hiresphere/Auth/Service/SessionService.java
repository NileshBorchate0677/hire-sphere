package com.hiresphere.hiresphere.Auth.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import com.hiresphere.hiresphere.Auth.Entity.Session;
import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Repository.SessionRepository;


import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SessionService {

	private final SessionRepository sessionRepository;
	
	private final int session_limit = 3;
	
	
	// created the new Session
	
	public void genrateNewSession(Users user, String refreshToken)
	{
		List<Session> userSession = sessionRepository.findByUser(user);
		
		if(userSession.size()== session_limit)
		{
			userSession.sort(Comparator.comparing(Session::getLastCreatedAt));
			
			Session firstCreatedSession=userSession.get(0);
			sessionRepository.delete(firstCreatedSession);
			
			Session newSession = Session.builder()
					.user(user)
					.refreshToken(refreshToken)
					.build();
			
			sessionRepository.save(newSession);
			
		} 
		
		 
		
		
	}


	
	// validate the session from refresh API through the Cookie
	public void validateRefreshToken(String refreshToken) {
		
		Session session=(sessionRepository.findByRefreshToken(refreshToken)
				.orElseThrow(() -> new SessionAuthenticationException(
						"The Session is not match for the refresh token"+refreshToken)));
				
		session.setLastCreatedAt(LocalDateTime.now());
		
		sessionRepository.save(session);
		
	} 
}
