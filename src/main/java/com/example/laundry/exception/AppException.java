package com.example.laundry.exception;

/**
 * Base application exception for unchecked runtime errors in the laundry app.
 * Use subclasses for specific semantics (NotFound, Validation, etc.).
 */
public class AppException extends RuntimeException {
    public AppException() { super(); }
    public AppException(String message) { super(message); }
    public AppException(String message, Throwable cause) { super(message, cause); }
    public AppException(Throwable cause) { super(cause); }
}
