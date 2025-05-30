package com.flightfinder.backend.exception;

public class AmadeusApiErrorException extends RuntimeException {
    public AmadeusApiErrorException(String message) {
        super(message);
    }

    public AmadeusApiErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}