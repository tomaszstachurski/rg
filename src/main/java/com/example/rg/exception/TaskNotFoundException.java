package com.example.rg.exception;

public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException(String id) {
        super(String.format("Task with id: %s not found!", id));
    }
}
