package com.projectboated.backend.web.kanban.kanbanlane.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CreateKanbanLaneRequest {

    @NotEmpty
    private String name;

}
