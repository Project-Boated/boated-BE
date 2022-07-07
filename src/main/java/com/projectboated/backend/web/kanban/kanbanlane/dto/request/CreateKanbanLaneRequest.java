package com.projectboated.backend.web.kanban.kanbanlane.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class CreateKanbanLaneRequest {

    @NotBlank
    @Size(min = 2, max = 20)
    private String name;

}
