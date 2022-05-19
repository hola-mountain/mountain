package com.example.mountain.exception;

import org.springframework.http.HttpStatus;

public class NoDataFounedException extends GlobalMountainException {

    public NoDataFounedException() {
        super(HttpStatus.BAD_REQUEST);
    }

    public NoDataFounedException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

    public NoDataFounedException(String reason, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, reason, cause);
    }

    public NoDataFounedException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
