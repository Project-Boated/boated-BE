package org.projectboated.backend.web.task.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.projectboated.backend.domain.task.entity.Task;

@NoArgsConstructor
@Getter
public class CreateTaskResponse {

    private Long id;

    public CreateTaskResponse(Task task) {
        this.id = task.getId();
    }
}
