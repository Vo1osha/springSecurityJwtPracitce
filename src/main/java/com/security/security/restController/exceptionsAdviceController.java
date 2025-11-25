package com.security.security.restController;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class exceptionsAdviceController {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,String>> handleRunTimeException(RuntimeException ex){

        Map<String,String> errors = new HashMap<String,String>();
        log.error("Runtime exception in login: ", ex);
        errors.put("error","Auth failed");
        errors.put("message",ex.getMessage());
        errors.put("timestamp", Instant.now().toString());

        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }
}
