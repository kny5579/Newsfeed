package com.example.newsfeed.common.exception;

import org.springframework.http.HttpStatus;

public class SamePasswordException extends ApplicationException {
    public SamePasswordException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
