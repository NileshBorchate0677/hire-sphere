package com.hiresphere.hiresphere.Auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hiresphere.hiresphere.Application.Repository.ApplicationRepository;
import com.hiresphere.hiresphere.Auth.Dto.DeleteAccountRequestDto;
import com.hiresphere.hiresphere.Auth.Dto.UpdateNameRequestDto;
import com.hiresphere.hiresphere.Auth.Dto.UserDto;
import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Enums.UserRoles;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Auth.Service.SessionService;
import com.hiresphere.hiresphere.Auth.Service.UserService;
import com.hiresphere.hiresphere.Job.Repository.JobRepository;
import com.hiresphere.hiresphere.JobSeeker.Repository.JobSeekerRepository;
import com.hiresphere.hiresphere.Recruiter.Repository.RecruiterRepository;
import com.hiresphere.hiresphere.SavedJob.Repository.SavedJobRepository;

@ExtendWith(MockitoExtension.class)
class AccountSettingsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SessionService sessionService;

    @Mock
    private SavedJobRepository savedJobRepository;

    @Mock
    private JobSeekerRepository jobSeekerRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private com.hiresphere.hiresphere.Auth.Repository.PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private com.hiresphere.hiresphere.Notification.Repository.NotificationRepository notificationRepository;

    @Mock
    private com.hiresphere.hiresphere.Auth.Service.EmailService emailService;

    @InjectMocks
    private UserService userService;

    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setUserId(5L);
        user.setEmail("user@example.com");
        user.setName("Old Name");
        user.setPassword("hashed_secret_password");
        user.setRole(UserRoles.JOB_SEEKER);
    }

    private void mockSecurityContext(String email) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Should update user display name successfully")
    void testUpdateName_Success() {
        mockSecurityContext("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateNameRequestDto dto = new UpdateNameRequestDto();
        dto.setName("New Full Name");

        UserDto result = userService.updateName(dto);

        assertNotNull(result);
        assertEquals("New Full Name", result.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should successfully delete account and purge all relations when password is correct")
    void testDeleteAccount_Success() {
        mockSecurityContext("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("mypassword", "hashed_secret_password")).thenReturn(true);
        when(jobSeekerRepository.findByUser(user)).thenReturn(Optional.empty());
        when(recruiterRepository.findByUser(user)).thenReturn(Optional.empty());

        DeleteAccountRequestDto dto = new DeleteAccountRequestDto();
        dto.setPassword("mypassword");

        userService.deleteAccount(dto);

        verify(savedJobRepository, times(1)).deleteByUser(user);
        verify(sessionService, times(1)).logoutAllDevices(user);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("Should throw exception and abort account deletion when password is wrong")
    void testDeleteAccount_WrongPassword_ThrowsException() {
        mockSecurityContext("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong_password", "hashed_secret_password")).thenReturn(false);

        DeleteAccountRequestDto dto = new DeleteAccountRequestDto();
        dto.setPassword("wrong_password");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.deleteAccount(dto));
        assertTrue(ex.getMessage().contains("Incorrect password"));
        verify(userRepository, never()).delete(any(Users.class));
    }
}
