package my.sleepydeveloper.projectcompass.web.project.dto;

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
