package com.example.laundry.exception;

/**
 * Thrown when a requested resource (student, order, etc.) cannot be found.
 */
public class NotFoundException extends AppException {
    public NotFoundException() { super(); }
    public NotFoundException(String message) { super(message); }
    public NotFoundException(String message, Throwable cause) { super(message, cause); }
}
