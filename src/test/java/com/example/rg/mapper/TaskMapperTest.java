package com.example.rg.mapper;

import com.example.rg.dto.TaskDto;
import com.example.rg.model.Status;
import com.example.rg.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TaskMapperImpl.class)
public class TaskMapperTest {

    private TaskMapper taskMapper;

    @Autowired
    public TaskMapperTest(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Test
    void testToEntity() {
        // given
        var taskDto = new TaskDto("title", "descrption", Status.CREATED);

        // when
        var entity = taskMapper.toEntity(taskDto);

        // then
        assertThat(entity.getTitle()).isEqualTo("title");
        assertThat(entity.getDescription()).isEqualTo("descrption");
        assertThat(entity.getStatus()).isEqualTo(Status.CREATED);
    }

    @Test
    void testToSavedTaskDto() {
        // given
        var date = LocalDateTime.now();
        var task = new Task("id", "title", "descrption", date, Status.CREATED);

        // when
        var savedTaskDto = taskMapper.toSavedTaskDto(task);

        // then
        assertThat(savedTaskDto.getId()).isEqualTo("id");
        assertThat(savedTaskDto.getTitle()).isEqualTo("title");
        assertThat(savedTaskDto.getDescription()).isEqualTo("descrption");
        assertThat(savedTaskDto.getCreationDate()).isEqualTo(date);
        assertThat(savedTaskDto.getStatus()).isEqualTo(Status.CREATED);
    }
}
