package com.example.mountain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GlobalMountainException extends ResponseStatusException {
    public GlobalMountainException(HttpStatus status) {
        super(status);
    }

    public GlobalMountainException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public GlobalMountainException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public GlobalMountainException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
