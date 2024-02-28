package com.luizventura.todosimple.exceptions;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@ExceptionHandler(DataBidingViolationException.class)
import com.luizventura.todosimple.services.exceptions.DataBidingViolationException;
import com.luizventura.todosimple.services.exceptions.ObjectNotFoundException;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;

// import org.apache.commons.lang3.exception.ExceptionUtils;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER") //logger from Lombook that prints in the console notes from the class
@RestControllerAdvice // it warns spring that this class needs to be initialized with spring
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @Value("${server.error.include-exception}") // it's set up as true at application properties to print printStackTrace
    private boolean printStackTrace;

    @Override // overwriting the handleMethodArgumentNotValid that is in ResponseEntityExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException, //o método vai capturar qualquer tipo de exceção que for um argumento inválido. Esse padrão vai retornar o ResponseEntity, uma entidade do tipo objeto
            HttpHeaders headers, //pega headers and status, and the request.
            HttpStatus status,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(), //getting value and printing a message
                "Validation error. Check 'errors' field for details.");
        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) { //for adding all exceptions to the errors objets from ErrorResponse.java
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse); //returns that it was an entity not possible to process
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception,
            WebRequest request) {
        final String errorMessage = "Unknown error occurred"; //if our program generated any exception that we havent treated before, or that we didn't know existed, we're gonna return "Unknown error occurred". Then we're gonna do the log
        log.error(errorMessage, exception); // log Slf4j and print the message and the exception in the console. - application.properties -> server.error.include-exception=false
        return buildErrorResponse( //build do erro
                exception,
                errorMessage,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // error 422
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException constraintViolationException, //for user being created without password.
            WebRequest request) {
        log.error("Failed to validate element", constraintViolationException);
        return buildErrorResponse( //buildErrorResponse that has 3 parameters. We don't pass the message, it's going to be the one generated automatically by the exception
                constraintViolationException,
                HttpStatus.UNPROCESSABLE_ENTITY,
                request);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException objectNotFoundException,
            WebRequest request) {
        log.error("Failed to find the requested element", objectNotFoundException);
        return buildErrorResponse(
                objectNotFoundException,
                HttpStatus.NOT_FOUND,
                request);
    }

    @ExceptionHandler(DataBidingViolationException.class) 
    @ResponseStatus(HttpStatus.CONFLICT)    // entidade User relacionada com Task
    public ResponseEntity<Object> handleDataBindingViolationException(
            DataBidingViolationException dataBindingViolationException,
            WebRequest request) {
        log.error("Failed to save entity with associated data", dataBindingViolationException);
        return buildErrorResponse(
                dataBindingViolationException,
                HttpStatus.CONFLICT,
                request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class) //     ex: user with the same username
    @ResponseStatus(HttpStatus.CONFLICT) //CONFLICT is the 409 status
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException dataIntegrityViolationException, //recieve error/exception
            WebRequest request) { //recieve request
        String errorMessage = dataIntegrityViolationException.getMostSpecificCause().getMessage(); // capture message error
        log.error("Failed to save entity with integrity problems: " + errorMessage, dataIntegrityViolationException);
        return buildErrorResponse(
                dataIntegrityViolationException,
                errorMessage,
                HttpStatus.CONFLICT,
                request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
        if (this.printStackTrace) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));// "ExceptionUtils" is from the dependency "commons-lang3". It catches the exception
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
