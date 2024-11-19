package com.istafrica.signature.exception;

public class CustomExceptions {
    
    // For conflicts like duplicate emails, etc.
    public static class ResourceConflictException extends RuntimeException {
        public ResourceConflictException(String message) {
            super(message);
        }
    }

    // For when requested resources don't exist
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    // For business logic validation failures
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    // For authentication and authorization failures
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
    
}