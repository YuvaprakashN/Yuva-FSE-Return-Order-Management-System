package com.roms.microservice.authenticationservice.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {



    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
    	log.warn("User Not Found exception: "+ex.getMessage());
        ExceptionResponse e = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false),
                HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenInvalidException.class)
    public final ResponseEntity<ExceptionResponse> handleTokenInvalidException(TokenInvalidException ex, WebRequest request) {
      log.info("TOKEN INVALID EXCEPTION: "+ex.getMessage());
    	ExceptionResponse e = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false),
                HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(e, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(ConstraintsInvalidException.class)
    public final ResponseEntity<ExceptionResponse> handleConstraintsInvalidException(ConstraintsInvalidException ex, WebRequest request) {
    	log.info("CONSTRAINTS EXCEPTION: "+ex.getMessage());
    	ExceptionResponse e = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false),
                HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(e, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
    	log.info("EXCEPTION OCCURED: "+ex.getMessage());
        ExceptionResponse e = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
