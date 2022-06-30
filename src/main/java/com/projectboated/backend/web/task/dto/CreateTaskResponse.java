package com.projectboated.backend.web.task.dto;

import com.projectboated.backend.domain.task.entity.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateTaskResponse {

    private Long id;

    public CreateTaskResponse(Task task) {
        this.id = task.getId();
    }
}
