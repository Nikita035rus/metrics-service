package com.mtp.metricsservice.exception;

public class InvalidClientIdException extends RuntimeException {
    public InvalidClientIdException(String message) {
        super(message);
    }
}
