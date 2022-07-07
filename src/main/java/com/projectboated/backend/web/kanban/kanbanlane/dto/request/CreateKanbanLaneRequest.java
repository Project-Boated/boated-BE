package com.projectboated.backend.web.kanban.kanbanlane.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CreateKanbanLaneRequest {

    @NotBlank
    @Size(min = 2, max = 20)
    private String name;

    @Builder
    public CreateKanbanLaneRequest(String name) {
        this.name = name;
    }
}
