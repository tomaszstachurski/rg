package com.example.rg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TaskNotFoundExceptionHandler {

    @ExceptionHandler(value = { TaskNotFoundException.class })
    public ResponseEntity handleException(TaskNotFoundException e) {
        Map<String, String> body = new HashMap<>();
        body.put("message", e.getMessage());
        return new ResponseEntity(body, HttpStatus.NOT_FOUND);
    }
}
