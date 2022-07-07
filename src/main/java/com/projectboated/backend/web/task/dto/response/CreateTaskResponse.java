package com.projectboated.backend.web.task.dto.response;

import com.projectboated.backend.domain.task.entity.Task;
import lombok.Getter;

@Getter
public class CreateTaskResponse {

    private Long id;

    public CreateTaskResponse(Task task) {
        this.id = task.getId();
    }
}
