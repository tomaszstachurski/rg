package com.example.rg.service;

import com.example.rg.dto.SavedTaskDto;
import com.example.rg.dto.TaskDto;
import com.example.rg.mapper.TaskMapper;
import com.example.rg.model.Status;
import com.example.rg.model.Task;
import com.example.rg.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskServiceTest {

    private final TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private TaskMapper taskMapper;

    @Autowired
    public TaskServiceTest(TaskService taskService) {
        this.taskService = taskService;
    }

    @Test
    void testGetTask() {
        // given
        var id = "id";
        var date = LocalDateTime.now();
        var task = new Task("id", "title", "description", date, Status.CREATED);
        var savedTaskDto = new SavedTaskDto("id", "title", "description", date, Status.CREATED);

        when(taskRepository.findById(id)).thenReturn(Mono.just(task));
        when(taskMapper.toSavedTaskDto(task)).thenReturn(savedTaskDto);

        // when
        var result = taskService.getTask(id);

        // then
        StepVerifier.create(result)
                .expectNext(savedTaskDto)
                .expectComplete()
                .verify();
    }

    @Test
    void testGetTasks() {
        // given
        var id = "id";
        var date = LocalDateTime.now();
        var task = new Task(id, "title", "description", date, Status.CREATED);
        var savedTaskDto = new SavedTaskDto(id, "title", "description", date, Status.CREATED);

        when(taskRepository.findAll()).thenReturn(Flux.just(task));
        when(taskMapper.toSavedTaskDto(task)).thenReturn(savedTaskDto);

        // when
        var result = taskService.getTasks();

        // then
        StepVerifier.create(result)
                .expectNext(savedTaskDto)
                .expectComplete()
                .verify();
    }

    @Test
    void testCreateTask() {
        // given
        var id = "id";
        var date = LocalDateTime.now();
        var taskDto = new TaskDto("title", "description", Status.DONE);
        var task = new Task("id", "title", "description", date, Status.DONE);
        var savedTaskDto = new SavedTaskDto("id", "title", "description", date, Status.DONE);

        when(taskRepository.save(task)).thenReturn(Mono.just(task));
        when(taskMapper.toEntity(taskDto)).thenReturn(task);
        when(taskMapper.toSavedTaskDto(task)).thenReturn(savedTaskDto);

        // when
        var result = taskService.createTask(taskDto);

        // then
        StepVerifier.create(result)
                .expectNext(savedTaskDto)
                .expectComplete()
                .verify();
    }

    @Test
    void testUpdateTask() {
        // given
        var id = "id";
        var date = LocalDateTime.now();
        var taskDto = new TaskDto("title2", "description2", Status.DONE);
        var existingTask = new Task("id", "title", "description", date, Status.DONE);
        var expectedTask = new Task("id", "title2", "description2", date, Status.DONE);
        var expectedTaskDto = new SavedTaskDto("id", "title2", "description2", date, Status.DONE);

        when(taskRepository.findById(id)).thenReturn(Mono.just(existingTask));
        when(taskRepository.save(any())).thenReturn(Mono.just(expectedTask));
        when(taskMapper.toSavedTaskDto(any())).thenReturn(expectedTaskDto);

        // when
        var result = taskService.updateTask(id, taskDto);

        // then
        StepVerifier.create(result)
                .expectNext(expectedTaskDto)
                .expectComplete()
                .verify();
    }

    @Test
    void testDeleteTask() {
        // given
        var id = "id";
        var date = LocalDateTime.now();
        var taskDto = new TaskDto("title2", "description2", Status.DONE);
        var existingTask = new Task("id", "title", "description", date, Status.DONE);
        var expectedTask = new Task("id", "title2", "description2", date, Status.DONE);
        var expectedTaskDto = new SavedTaskDto("id", "title2", "description2", date, Status.DONE);

        when(taskRepository.findById(id)).thenReturn(Mono.just(existingTask));
        when(taskRepository.delete(any())).thenReturn(Mono.empty());

        // when
        var result = taskService.deleteTask(id);

        // then
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
}