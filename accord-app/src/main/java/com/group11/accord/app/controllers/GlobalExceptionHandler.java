package com.group11.accord.app.controllers;

import com.group11.accord.api.errors.ProblemDetailFactory;
import com.group11.accord.api.errors.ValidationDetail;
import com.group11.accord.api.errors.ValidationProcessor;
import com.group11.accord.app.exceptions.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(ServerErrorException.class)
    public ProblemDetail serverErrorException(ServerErrorException e) {
        return ProblemDetailFactory.createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(FileProcessingException.class)
    public ProblemDetail fileProcessingException(FileProcessingException e) {
        return ProblemDetailFactory.createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ProblemDetail invalidOperationException(InvalidOperationException e) {
        return ProblemDetailFactory.createProblemDetail(HttpStatus.CONFLICT, e);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        ValidationDetail body = ProblemDetailFactory.createValidationDetail(ex.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale()));
        ValidationProcessor.addErrorsToValidationDetail(ex.getGlobalErrors(), ex.getFieldErrors(), body);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
        HandlerMethodValidationException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        ValidationDetail body = ProblemDetailFactory.createValidationDetail(ex.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale()));
        ValidationProcessor.addValidationResultsToValidationDetail(ex.getAllValidationResults(), body);
        return handleExceptionInternal(ex, body, headers, status, request);
    }
}
