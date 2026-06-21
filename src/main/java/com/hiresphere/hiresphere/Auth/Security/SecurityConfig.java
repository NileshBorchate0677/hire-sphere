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

import java.util.List;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig {

    private final JwtAtenticationFilter jwtAtenticationFilter;
 
    
    
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
                            "/user/auth/refresh"
                    ).permitAll()

                    .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/recruiter/**").hasAuthority("ROLE_RECRUITER")
                    .requestMatchers("/api/job-seeker/**").hasAuthority("ROLE_JOB_SEEKER")

                    .requestMatchers(
                            "/user/auth/logout",
                            "/user/auth/logoutAll",
                            "/user/auth/change-password"
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
                List.of(
                        "http://localhost:5173",
                        "http://localhost:5174"
                ));

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        configuration.setAllowedHeaders(
                List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    
}