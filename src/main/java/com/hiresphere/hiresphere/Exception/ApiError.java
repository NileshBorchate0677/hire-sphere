package com.hiresphere.hiresphere.Exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private HttpStatus statusCode;
    private String error;
    private String message;
    private String path;
    private Map<String, String> errors;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(String error, HttpStatus statusCode) {
        this();
        this.error = error;
        this.message = error;
        this.statusCode = statusCode;
        this.status = statusCode != null ? statusCode.value() : 500;
    }

    public ApiError(String message, String error, HttpStatus statusCode, String path) {
        this();
        this.message = message;
        this.error = error;
        this.statusCode = statusCode;
        this.status = statusCode != null ? statusCode.value() : 500;
        this.path = path;
    }

    public ApiError(String message, String error, HttpStatus statusCode, String path, Map<String, String> errors) {
        this(message, error, statusCode, path);
        this.errors = errors;
    }

    // Backward compatibility for code calling getTimeStamp() or setTimeStamp()
    public LocalDateTime getTimeStamp() {
        return this.timestamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timestamp = timeStamp;
    }
}