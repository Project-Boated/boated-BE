package my.sleepydeveloper.projectcompass.web.project.dto;

import lombok.*;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProjectResponse {

    private Long id;

    public CreateProjectResponse(Project project) {
        this.id = project.getId();
    }
}
