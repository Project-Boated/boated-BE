package com.projectboated.backend.task.task.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskUpdateRequest {

    private String name;
    private String description;
    private LocalDateTime deadline;

    private Long laneId;

    @Builder
    public TaskUpdateRequest(String name, String description, LocalDateTime deadline, Long laneId) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.laneId = laneId;
    }

}
