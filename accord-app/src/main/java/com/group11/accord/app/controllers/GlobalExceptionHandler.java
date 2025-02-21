package com.group11.accord.app.controllers;

import com.group11.accord.api.errors.ProblemDetailFactory;
import com.group11.accord.app.exceptions.AccountNotAuthorizedException;
import com.group11.accord.app.exceptions.InvalidCredentialsException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//IMPORTANT This code was entirely written by William regardless of who committed

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail entityNotFoundException(EntityNotFoundException e) {
        return ProblemDetailFactory.createProblemDetail(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ProblemDetail entityExistsException(EntityExistsException e) {
        return ProblemDetailFactory.createProblemDetail(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail invalidCredentialsException(InvalidCredentialsException e) {
        // We could throw a 403 Forbidden here, but 401 Unauthorized hides the existence of the resource
        return ProblemDetailFactory.createProblemDetail(HttpStatus.UNAUTHORIZED, e);
    }

    //For when an account attempts to make a request with a resource they are not authorized to make
    @ExceptionHandler(AccountNotAuthorizedException.class)
    public ProblemDetail accountNotAuthorizedException(AccountNotAuthorizedException e) {
        return ProblemDetailFactory.createProblemDetail(HttpStatus.FORBIDDEN, e);
    }
}
