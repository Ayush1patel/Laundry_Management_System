package com.example.laundry.exception;

/**
 * Thrown when input validation fails (bad weight, missing services, etc.).
 * Controllers can map this to HTTP 400.
 */
public class ValidationException extends AppException {
    public ValidationException() { super(); }
    public ValidationException(String message) { super(message); }
    public ValidationException(String message, Throwable cause) { super(message, cause); }
}
