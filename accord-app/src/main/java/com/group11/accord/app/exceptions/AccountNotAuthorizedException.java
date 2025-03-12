package com.group11.accord.app.exceptions;

public class AccountNotAuthorizedException extends RuntimeException {
    public AccountNotAuthorizedException(String message) {
        super(message);
    }
}
