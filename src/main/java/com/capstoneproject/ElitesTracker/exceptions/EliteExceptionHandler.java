package com.capstoneproject.ElitesTracker.exceptions;

import com.capstoneproject.ElitesTracker.dtos.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class EliteExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<?> exceptionHandler(Exception exception){
        var response = ApiResponse.builder().data(exception.getMessage()).build();
        return ResponseEntity.badRequest().body(response);
    }
}
