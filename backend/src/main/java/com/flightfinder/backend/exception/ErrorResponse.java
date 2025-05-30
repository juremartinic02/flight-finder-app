package com.flightfinder.backend.exception;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for a standardized error response that is sent to the client.
 * This is used by the GlobalExceptionHandler.java to provide consistent error to the client.
 */
public class ErrorResponse {
    // Timestamp for when the error occured
    private LocalDateTime timestamp;
    // HTTP status code that is associated with the error
    private int status;
    // Error type like "Bad request", etc.
    private String error;
    // Message explaining the error
    private String message;
    // The request path that resulted in the error
    private String path;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
