package com.projectboated.backend.web.task.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.domain.task.task.service.dto.TaskUpdateRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class PatchTaskRequest {

    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;
    private Long laneId;

    @Builder
    public PatchTaskRequest(String name, String description, LocalDateTime deadline, Long landId) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.laneId = landId;
    }

    public TaskUpdateRequest toTaskUpdateRequest() {
        return TaskUpdateRequest.builder()
                .name(this.name)
                .description(this.description)
                .deadline(this.deadline)
                .build();
    }


}
