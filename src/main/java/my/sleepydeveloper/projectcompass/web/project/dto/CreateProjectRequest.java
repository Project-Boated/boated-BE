package my.sleepydeveloper.projectcompass.web.project.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProjectRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;
}
