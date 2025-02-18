package com.example.newsfeed.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordFormatException extends ApplicationException{
    public InvalidPasswordFormatException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
