package my.sleepydeveloper.projectcompass.web.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyProjectResponse {

    private List<ProjectResponse> projects;
}
