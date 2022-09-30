package com.projectboated.backend.task.task.controller.dto.response;

import com.projectboated.backend.task.task.entity.Task;
import lombok.Getter;

@Getter
public class CreateTaskResponse {

    private Long id;

    public CreateTaskResponse(Task task) {
        this.id = task.getId();
    }
}
