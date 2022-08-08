package com.projectboated.backend.domain.task.task.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskUpdateRequest {

    private String name;
    private String description;
    private LocalDateTime deadline;

    @Builder
    public TaskUpdateRequest(String name, String description, LocalDateTime deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }

}
