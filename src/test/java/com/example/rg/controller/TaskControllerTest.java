package com.example.rg.controller;

import com.example.rg.dto.SavedTaskDto;
import com.example.rg.dto.TaskDto;
import com.example.rg.exception.TaskNotFoundException;
import com.example.rg.model.Status;
import com.example.rg.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(TaskController.class)
class TaskControllerTest {

    private final TaskController taskController;

    private WebTestClient webTestClient;

    @MockBean
    private TaskService taskService;

    @Autowired
    public TaskControllerTest(TaskController taskController, WebTestClient webTestClient) {
        this.taskController = taskController;
        this.webTestClient = webTestClient;
    }

    @Test
    void testGetTasks() throws Exception {
        // given
        var dateTime = LocalDateTime.now();
        var task = new SavedTaskDto("id", "Title", "Description", dateTime, Status.CREATED);

        // when
        when(taskService.getTasks()).thenReturn(Flux.just(task));

        // then
        webTestClient.get()
                        .uri("/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBodyList(SavedTaskDto.class).hasSize(1).contains(task);
    }

    @Test
    void testGetTask() throws Exception {
        // given
        var dateTime = LocalDateTime.now();
        var task = new SavedTaskDto("id", "Title", "Description", dateTime, Status.CREATED);

        // when
        when(taskService.getTask(any())).thenReturn(Mono.just(task));

        // then
        SavedTaskDto response = webTestClient.get()
                .uri("/tasks" + "/id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(SavedTaskDto.class)
                .getResponseBody()
                .blockFirst();

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("id");
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getDescription()).isEqualTo("Description");
        assertThat(response.getCreationDate()).isEqualTo(dateTime);
        assertThat(response.getStatus()).isEqualTo(Status.CREATED);
    }

    @Test
    void testGetTaskNotFound() throws Exception {
        // given

        // when
        when(taskService.getTask(any())).thenReturn(Mono.error(new TaskNotFoundException("id")));

        // then
        webTestClient.get()
                .uri("/tasks" + "/id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateTask() {
        // given
        var dateTime = LocalDateTime.now();
        var body = new TaskDto("Title", "Description", Status.CREATED);
        var task = new SavedTaskDto("id", "Title", "Description", dateTime, Status.CREATED);

        // when
        when(taskService.createTask(any())).thenReturn(Mono.just(task));

        // then
        SavedTaskDto response = webTestClient.post()
                .uri("/tasks")
                .body(BodyInserters.fromValue(body))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(SavedTaskDto.class)
                .getResponseBody()
                .blockFirst();

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("id");
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getDescription()).isEqualTo("Description");
        assertThat(response.getCreationDate()).isEqualTo(dateTime);
        assertThat(response.getStatus()).isEqualTo(Status.CREATED);
    }

    @Test
    void testUpdateTask() {
        // given
        var dateTime = LocalDateTime.now();
        var body = new TaskDto("Title", "Description", Status.CREATED);
        var task = new SavedTaskDto("id", "Title", "Description", dateTime, Status.CREATED);

        // when
        when(taskService.updateTask(any(), any())).thenReturn(Mono.just(task));

        // then
        SavedTaskDto response = webTestClient.put()
                .uri("/tasks" + "/id")
                .body(BodyInserters.fromValue(body))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(SavedTaskDto.class)
                .getResponseBody()
                .blockFirst();

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("id");
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getDescription()).isEqualTo("Description");
        assertThat(response.getCreationDate()).isEqualTo(dateTime);
        assertThat(response.getStatus()).isEqualTo(Status.CREATED);
    }

    @Test
    void testUpdateTaskNotFound() {
        // given
        var body = new TaskDto("Title", "Description", Status.CREATED);

        // when
        when(taskService.updateTask(any(), any())).thenReturn(Mono.error(new TaskNotFoundException("id")));

        // then
        webTestClient.put()
                .uri("/tasks" + "/id")
                .body(BodyInserters.fromValue(body))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteTask() {
        // given
        var dateTime = LocalDateTime.now();
        var task = new SavedTaskDto("id", "Title", "Description", dateTime, Status.CREATED);

        // when
        when(taskService.deleteTask(any())).thenReturn(Mono.empty());

        // then
        webTestClient.delete()
                .uri("/tasks" + "/id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

    }

    @Test
    void testDeleteTaskNotFound() throws Exception {
        // given when
        when(taskService.deleteTask(any())).thenReturn(Mono.error(new TaskNotFoundException("id")));

        // then
        webTestClient.delete()
                .uri("/tasks" + "/id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

}