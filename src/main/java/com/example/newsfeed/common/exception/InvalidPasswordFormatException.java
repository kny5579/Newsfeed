package com.example.newsfeed.common.exception;

public class InvalidPasswordFormatException extends RuntimeException{
    public InvalidPasswordFormatException(String message) {
        super(message);
    }
}
