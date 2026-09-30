package com.hiresphere.hiresphere.Auth.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hiresphere.hiresphere.Auth.Entity.PasswordResetToken;
import com.hiresphere.hiresphere.Auth.Entity.Users;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUser(Users user);
}
