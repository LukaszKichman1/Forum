package com.example.Forum.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserCanNotBeCreateException.class)
    public ResponseEntity<Object> handleUserCanNotBeCreateException(UserCanNotBeCreateException ex, WebRequest webRequest)
    {
        return new ResponseEntity<Object>(new RequestError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserCanNotBeActivationException.class)
    public ResponseEntity<Object> handleUserCanNotBeActivationException(UserCanNotBeActivationException ex, WebRequest webRequest)
    {
        return new ResponseEntity<Object>(new RequestError(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE, LocalDateTime.now()),HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest webRequest)
    {
        return new ResponseEntity<Object>(new RequestError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Object> handlePostNotFoundException(PostNotFoundException ex, WebRequest webRequest)
    {
        return new ResponseEntity<Object>(new RequestError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserIsNotOwnerException.class)
    public ResponseEntity<Object> handleUserIsNotOwnerException(UserIsNotOwnerException ex, WebRequest webRequest)
    {
        return new ResponseEntity<Object>(new RequestError(ex.getMessage(), HttpStatus.CONFLICT, LocalDateTime.now()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Object> handleCommentNotFoundException(CommentNotFoundException ex, WebRequest webRequest)
    {
        return new ResponseEntity<Object>(new RequestError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()),HttpStatus.NOT_FOUND);
    }

}