package com.group11.accord.app.exceptions;

//IMPORTANT This code was entirely written by William regardless of who committed

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}