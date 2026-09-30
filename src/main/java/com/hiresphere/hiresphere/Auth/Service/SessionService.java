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
		
		if(userSession.size() >= session_limit)
		{
			userSession.sort(Comparator.comparing(Session::getLastCreatedAt));
			
			Session firstCreatedSession=userSession.get(0); 
			sessionRepository.delete(firstCreatedSession);
			
			
		} 
		
		Session newSession = Session.builder()
				.user(user)
				.refreshToken(refreshToken)
				.build();
		
		sessionRepository.save(newSession);
		
		 
		
		
	}


	
	// validate the session from refresh API through the Cookie
	public void validateRefreshToken(String refreshToken) {
		
		Session session=(sessionRepository.findByRefreshToken(refreshToken)
				.orElseThrow(() -> new SessionAuthenticationException(
						"Session not found or expired. Please log in again.")));
				
		session.setLastCreatedAt(LocalDateTime.now());
		
		sessionRepository.save(session);
		
	} 
	
	
	
	
	public void logout( String refreshToken) {
		
	    Session session = sessionRepository.findByRefreshToken( refreshToken)
	                    .orElseThrow(() -> new RuntimeException( "Session Not Found"));

	    sessionRepository.delete(session);
	}
	
	
	
	
	public void logoutAllDevices( Users user){
		
	    List<Session> sessions = sessionRepository.findByUser(user);

	    sessionRepository.deleteAll(sessions); 
	} 

	public List<Session> getUserSessions(Users user) {
	    return sessionRepository.findByUser(user);
	}

	public void deleteSessionById(Users user, Long sessionId) {
	    Session session = sessionRepository.findById(sessionId)
	            .orElseThrow(() -> new RuntimeException("Session not found"));

	    if (!session.getUser().getUserId().equals(user.getUserId())) {
	        throw new RuntimeException("Unauthorized to terminate this session");
	    }

	    sessionRepository.delete(session);
	}
} 
