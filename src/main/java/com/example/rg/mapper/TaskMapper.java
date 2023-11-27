package com.example.rg.mapper;

import com.example.rg.dto.SavedTaskDto;
import com.example.rg.dto.TaskDto;
import com.example.rg.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface TaskMapper {

    @Mapping(target = "creationDate", expression = "java(LocalDateTime.now())")
    Task toEntity(TaskDto taskDto);

    SavedTaskDto toSavedTaskDto(Task task);
}
