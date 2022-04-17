package my.sleepydeveloper.projectcompass.web.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchProjectRequest {

    private String name;

    private String description;
}
