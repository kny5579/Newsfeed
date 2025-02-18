package com.example.newsfeed.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApplicationException{
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
