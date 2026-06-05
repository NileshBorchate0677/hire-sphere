package com.hiresphere.hiresphere.Exception;

import java.nio.file.AccessDeniedException;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.AuthenticationException;


import io.jsonwebtoken.JwtException;

@RestControllerAdvice 
public class GlobalExceptionHandler {

	//Handled user name not found 
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFoundException(
            UsernameNotFoundException ex) {

        ApiError apiError = new ApiError(
                "Invalid username or password", 
                HttpStatus.UNAUTHORIZED
        );

        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    
    //Handled BadCredentialException
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(
            BadCredentialsException ex) {

        ApiError apiError = new ApiError(
                "Invalid username or password",
                HttpStatus.UNAUTHORIZED
        );

        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handled Authentication Exception
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(
            AuthenticationException ex) {

        ApiError apiError = new ApiError(
                "Authentication failed",
                HttpStatus.UNAUTHORIZED
        );

        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    
    //Handled JWT Exception
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException ex) {

        ApiError apiError = new ApiError(
                "Invalid or expired JWT token",
                HttpStatus.UNAUTHORIZED
        );

        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    
    // Handled Access Denied Exception
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(
            AccessDeniedException ex) {

        ApiError apiError = new ApiError(
                "Access denied",
                HttpStatus.FORBIDDEN
        );

        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handled the Generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {

        ex.printStackTrace(); // trace internal error

        ApiError apiError = new ApiError(
                ex.getMessage(),   // TEMP: actual message
                HttpStatus.INTERNAL_SERVER_ERROR
        );

        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExistsException(
            EmailAlreadyExistException ex) {

        ApiError apiError = new ApiError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        ); 

        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }
    
}
 
  
