package com.hiresphere.hiresphere.Exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // =====================================================
    // 1. VALIDATION ERRORS (@Valid / Bean Validation)
    // =====================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        String primaryMessage = fieldErrors.isEmpty()
                ? "Validation failed"
                : fieldErrors.values().iterator().next();

        ApiError apiError = new ApiError(
                primaryMessage,
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI(),
                fieldErrors
        );

        log.warn("Validation error on path {}: {}", request.getRequestURI(), fieldErrors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // =====================================================
    // 2. RESOURCE NOT FOUND (404)
    // =====================================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ApiError apiError = new ApiError(
                ex.getMessage(),
                "RESOURCE_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                request.getRequestURI()
        );

        log.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecruiterProfileNotFoundException.class)
    public ResponseEntity<ApiError> handleRecruiterProfileNotFoundException(
            RecruiterProfileNotFoundException ex, HttpServletRequest request) {

        ApiError apiError = new ApiError(
                ex.getMessage(),
                "RECRUITER_PROFILE_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JobSeekerProfileNotFoundException.class)
    public ResponseEntity<ApiError> handleJobSeekerProfileNotFoundException(
            JobSeekerProfileNotFoundException ex, HttpServletRequest request) {

        ApiError apiError = new ApiError(
                ex.getMessage(),
                "JOB_SEEKER_PROFILE_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    // =====================================================
    // 3. UNAUTHORIZED OPERATION / ACCESS DENIED (403 FORBIDDEN)
    // =====================================================
    @ExceptionHandler({
            UnauthorizedOperationException.class,
            AccessDeniedException.class,
            java.nio.file.AccessDeniedException.class
    })
    public ResponseEntity<ApiError> handleAccessDeniedException(
            Exception ex, HttpServletRequest request) {

        String msg = (ex.getMessage() != null && !ex.getMessage().isBlank())
                ? ex.getMessage()
                : "You do not have permission to perform this action";

        ApiError apiError = new ApiError(
                msg,
                "FORBIDDEN",
                HttpStatus.FORBIDDEN,
                request.getRequestURI()
        );

        log.warn("Access denied on path {}: {}", request.getRequestURI(), msg);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    // =====================================================
    // 4. AUTHENTICATION & JWT (401 UNAUTHORIZED)
    // =====================================================
    @ExceptionHandler({
            BadCredentialsException.class,
            UsernameNotFoundException.class,
            AuthenticationException.class
    })
    public ResponseEntity<ApiError> handleAuthenticationException(
            Exception ex, HttpServletRequest request) {

        ApiError apiError = new ApiError(
                "Invalid username or password",
                "UNAUTHORIZED",
                HttpStatus.UNAUTHORIZED,
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(
            JwtException ex, HttpServletRequest request) {

        log.warn("JWT verification failed on path {}: {}", request.getRequestURI(), ex.getMessage());

        ApiError apiError = new ApiError(
                "Invalid or expired JWT token",
                "UNAUTHORIZED",
                HttpStatus.UNAUTHORIZED,
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    // =====================================================
    // 5. DUPLICATE RESOURCE / CONFLICT (409 CONFLICT)
    // =====================================================
    @ExceptionHandler({
            DuplicateResourceException.class,
            EmailAlreadyExistException.class
    })
    public ResponseEntity<ApiError> handleDuplicateResourceException(
            RuntimeException ex, HttpServletRequest request) {

        ApiError apiError = new ApiError(
                ex.getMessage(),
                "DUPLICATE_RESOURCE",
                HttpStatus.CONFLICT,
                request.getRequestURI()
        );

        log.warn("Duplicate resource conflict: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    // =====================================================
    // 6. BAD REQUEST & ARGUMENT ERRORS (400 BAD REQUEST)
    // =====================================================
    @ExceptionHandler({
            BadRequestException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiError> handleBadRequestException(
            RuntimeException ex, HttpServletRequest request) {

        ApiError apiError = new ApiError(
                ex.getMessage(),
                "BAD_REQUEST",
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // =====================================================
    // 7. RUNTIME / BUSINESS LOGIC ERRORS (FALLBACK ROUTER)
    // =====================================================
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {

        String msg = ex.getMessage() != null ? ex.getMessage() : "A business error occurred";
        String lowerMsg = msg.toLowerCase();

        HttpStatus status;
        String errorCode;

        if (lowerMsg.contains("not found")) {
            status = HttpStatus.NOT_FOUND;
            errorCode = "RESOURCE_NOT_FOUND";
        } else if (lowerMsg.contains("unauthorized") || lowerMsg.contains("forbidden")
                || lowerMsg.contains("only recruiter") || lowerMsg.contains("only job seeker")) {
            status = HttpStatus.FORBIDDEN;
            errorCode = "FORBIDDEN";
        } else if (lowerMsg.contains("already applied") || lowerMsg.contains("already exists")) {
            status = HttpStatus.CONFLICT;
            errorCode = "DUPLICATE_RESOURCE";
        } else {
            status = HttpStatus.BAD_REQUEST;
            errorCode = "BAD_REQUEST";
        }

        ApiError apiError = new ApiError(
                msg,
                errorCode,
                status,
                request.getRequestURI()
        );

        log.warn("Handled runtime exception ({} - {}): {}", status.value(), errorCode, msg);
        return new ResponseEntity<>(apiError, status);
    }

    // =====================================================
    // 8. UNCAUGHT GENERIC EXCEPTIONS (500 INTERNAL SERVER ERROR)
    // =====================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex, HttpServletRequest request) {

        log.error("Unhandled internal server error on path {}: ", request.getRequestURI(), ex);

        ApiError apiError = new ApiError(
                "An unexpected internal error occurred. Please try again later.",
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}