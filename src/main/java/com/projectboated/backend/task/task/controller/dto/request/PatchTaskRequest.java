package com.projectboated.backend.task.task.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.task.task.service.dto.TaskUpdateRequest;
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
    private String laneName;

    @Builder
    public PatchTaskRequest(String name, String description, LocalDateTime deadline, String laneName) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.laneName = laneName;
    }

    public TaskUpdateRequest toTaskUpdateRequest() {
        return TaskUpdateRequest.builder()
                .name(this.name)
                .description(this.description)
                .deadline(this.deadline)
                .laneName(this.laneName)
                .build();
    }


}
