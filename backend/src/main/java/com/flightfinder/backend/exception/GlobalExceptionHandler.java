package com.flightfinder.backend.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice; // Provides access to request details
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the Spring Boot application
 * Global exception handler acts like a centralized point for handling exceptions thrown from any controller
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // Logger instance for this class to log all handled exceptions
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles AmadeusApiErrorException which is thrown when there is an issue with communicating with the Amadeus API
     */
    @ExceptionHandler(AmadeusApiErrorException.class) // Specifies this method handles AmadeusApiErrorException
    public ResponseEntity<ErrorResponse> handleAmadeusApiErrorException(AmadeusApiErrorException ex, WebRequest request) {
        logger.error("Amadeus API Error Exception: {}", ex.getMessage(), ex); // Log for server-side debugging

        // Creating a standardized error response object
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // HTTP status code
                "Amadeus API Error",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "") // Request path where the error occurred
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handler which handles InvalidSearchRequestException which may be thrown if Amadeus rejects search parameters
     */
    @ExceptionHandler(InvalidSearchRequestException.class) // Specifies this method handles InvalidSearchRequestException
    public ResponseEntity<ErrorResponse> handleInvalidSearchRequestException(InvalidSearchRequestException ex, WebRequest request) {
        logger.error("Invalid Search Request Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Search Parameters",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handler for bean validation errors (@Valid on SearchRequest)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Validation Error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String detailedMessage = "Validation failed for request: " + errors.toString();
        
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                detailedMessage,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Generic handler for any other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unhandled Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.", // Generic message for unexpected errors
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
