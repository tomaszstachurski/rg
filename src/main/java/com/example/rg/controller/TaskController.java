package com.example.rg.controller;

import com.example.rg.dto.SavedTaskDto;
import com.example.rg.dto.TaskDto;
import com.example.rg.service.TaskService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "get all tasks") })
    @GetMapping
    public ResponseEntity<Flux<SavedTaskDto>> getTasks() {
        return ResponseEntity.ok(taskService.getTasks());
    }

    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "get task by id"),
                            @ApiResponse(responseCode = "404", description = "task not found") })
    @GetMapping("/{id}")
    public ResponseEntity<Mono<SavedTaskDto>> getTask(@PathVariable String id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "create task") })
    @PostMapping
    public ResponseEntity<Mono<SavedTaskDto>> createTask(@Valid @RequestBody TaskDto taskDto) {
        return new ResponseEntity<>(taskService.createTask(taskDto), HttpStatus.CREATED);
    }

    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "update task by id"),
                            @ApiResponse(responseCode = "404", description = "task not found") })
    @PutMapping("/{id}")
    public ResponseEntity<Mono<SavedTaskDto>> updateTask(@PathVariable String id, @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDto));
    }

    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "task deleted"),
                            @ApiResponse(responseCode = "404", description = "task not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Mono<Void>> deleteTask(@PathVariable String id) {
        return new ResponseEntity<>(taskService.deleteTask(id), HttpStatus.NO_CONTENT);
    }
}
