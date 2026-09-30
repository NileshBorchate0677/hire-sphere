package com.hiresphere.hiresphere.Auth.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig {

    private final JwtAtenticationFilter jwtAtenticationFilter;

    /** Comma-separated allowed CORS origins; set CORS_ALLOWED_ORIGINS env var in production */
    @Value("${cors.allowed.origins:http://localhost:5173}")
    private String corsAllowedOrigins;
 
    
    
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }

    
    
    
    @Bean
    SecurityFilterChain securityFilterChain( 
            HttpSecurity http)
            throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .cors(cors -> {})

            .sessionManagement(session ->
                    session.sessionCreationPolicy( 
                            SessionCreationPolicy.STATELESS))

            
            .authorizeHttpRequests(auth -> auth

                    .requestMatchers(
                            "/user/auth/register",
                            "/user/auth/login",
                            "/user/auth/refresh",
                            "/user/auth/forgot-password",
                            "/user/auth/reset-password",
                            "/Jobs/getAllJobs",
                            "/Jobs/getJob/*",
                            "/Jobs/search",
                            "/jobs/search",
                            "/Jobs/search/paged",
                            "/jobseeker/resume/download/**",
                            "/recruiter/resume/download/**",
                            "/jobseeker/taxonomy"
                    ).permitAll()

                    .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/recruiter/**").hasAuthority("ROLE_RECRUITER")
                    .requestMatchers("/analytics/**").hasAuthority("ROLE_RECRUITER")
                    .requestMatchers("/jobseeker/**").hasAuthority("ROLE_JOB_SEEKER")
                    .requestMatchers("/saved-jobs/**").authenticated()
                    .requestMatchers("/applications/**").authenticated()
                    .requestMatchers("/notifications/**").authenticated()
                    .requestMatchers("/Jobs/**").authenticated()

                    .requestMatchers(
                            "/user/auth/logout",
                            "/user/auth/logoutAll",
                            "/user/auth/change-password",
                            "/user/auth/me",
                            "/user/auth/update-name",
                            "/user/auth/sessions",
                            "/user/auth/sessions/*",
                            "/user/auth/delete-account"
                    ).authenticated()

                    .anyRequest().authenticated()
            )

            .addFilterBefore(
                    jwtAtenticationFilter,
                    UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    
    
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                Arrays.stream(corsAllowedOrigins.split(","))
                      .map(String::trim)
                      .filter(s -> !s.isEmpty())
                      .toList()
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        configuration.setAllowedHeaders(
                List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    
}