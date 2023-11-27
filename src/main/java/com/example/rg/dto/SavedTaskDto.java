package com.example.rg.dto;

import com.example.rg.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedTaskDto {

    private String id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private Status status;

}
