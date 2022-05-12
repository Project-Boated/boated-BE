package my.sleepydeveloper.projectcompass.web.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TerminateProjectResponse {

    private Long id;

    public TerminateProjectResponse(Long projectId) {
        this.id = projectId;
    }
}
