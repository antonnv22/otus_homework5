package ru.otus.profileservice.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.profileservice.exceptions.DuplicateUserException;
import ru.otus.profileservice.exceptions.NotFoundUserException;

@ControllerAdvice
public class UserControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String USER_NOT_FOUND = "User not found";
    private static final String DUPLICATE_USER = "Duplication user by Login";

    @ExceptionHandler(value = {NotFoundUserException.class})
    protected ResponseEntity<Object> handleUserNotFoundError(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, USER_NOT_FOUND, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {DuplicateUserException.class})
    protected ResponseEntity<Object> handleUserDuplicateError(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, DUPLICATE_USER, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
