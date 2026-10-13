/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.jsonpatchdemo;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * an exception handler for PersonNotFoundException that will return a 404
 * error.  Not something I would recommend in a real service, but good to
 * see how to do it.
 * 
 * @author aar1069
 */
@ControllerAdvice
public class PersonNotFoundExceptionHandler {
    
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFound(PersonNotFoundException ex) {
        ExceptionResponse r = new ExceptionResponse("not found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        
        return new ResponseEntity<>(r, HttpStatus.NOT_FOUND);
    }
}
