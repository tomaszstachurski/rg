package com.example.rg.service;

import com.example.rg.dto.SavedTaskDto;
import com.example.rg.dto.TaskDto;
import com.example.rg.exception.TaskNotFoundException;
import com.example.rg.mapper.TaskMapper;
import com.example.rg.model.Status;
import com.example.rg.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public Mono<SavedTaskDto> getTask(String id) {
        return taskRepository.findById(id).map(taskMapper::toSavedTaskDto).switchIfEmpty(Mono.error(new TaskNotFoundException(id)));
    }

    public Flux<SavedTaskDto> getTasks() {
        var tasks = taskRepository.findAll();
        return tasks.map(taskMapper::toSavedTaskDto);
    }

    public Mono<SavedTaskDto> createTask(TaskDto taskDto) {
        if(taskDto.getStatus() == null) {
            taskDto.setStatus(Status.CREATED);
        }
        var task = taskMapper.toEntity(taskDto);
        var saved = taskRepository.save(task);
        return saved.map(taskMapper::toSavedTaskDto);
    }

    public Mono<SavedTaskDto> updateTask(String id, TaskDto taskDto) {
        var existingTask = taskRepository.findById(id).switchIfEmpty(Mono.error(new TaskNotFoundException(id)));
        var saved = existingTask.flatMap(task -> {
            Optional.ofNullable(taskDto.getTitle()).ifPresent(task::setTitle);
            task.setDescription(taskDto.getDescription());
            task.setStatus(taskDto.getStatus());

            return taskRepository.save(task);
        });

        return saved.map(taskMapper::toSavedTaskDto);
    }

    public Mono<Void> deleteTask(String id) {
        return taskRepository.findById(id).flatMap(taskRepository::delete).switchIfEmpty(Mono.error(new TaskNotFoundException(id)));
    }
}
