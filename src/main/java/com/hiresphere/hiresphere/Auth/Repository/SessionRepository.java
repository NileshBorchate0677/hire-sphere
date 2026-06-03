package com.hiresphere.hiresphere.Auth.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hiresphere.hiresphere.Auth.Entity.Session;
import com.hiresphere.hiresphere.Auth.Entity.Users;

public interface SessionRepository extends JpaRepository<Session, Long> {
	
	List<Session> findByUser(Users user);

	Optional<Session> findByRefreshToken(String refreshToken);

	void deleteByUserAndRefreshTokenNot(Users user, String currentRefreshToken); 


}
