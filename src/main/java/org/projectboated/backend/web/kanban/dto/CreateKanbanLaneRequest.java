package org.projectboated.backend.web.kanban.dto;

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
