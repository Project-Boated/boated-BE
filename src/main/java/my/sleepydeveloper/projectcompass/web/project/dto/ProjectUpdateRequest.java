package my.sleepydeveloper.projectcompass.web.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdateRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;
}
