package my.sleepydeveloper.projectcompass.web.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetMyCaptainProjectResponse {

    private List<ProjectResponse> projects;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectResponse {

        private Long id;
        private String name;
        private String description;

        public ProjectResponse(Project project) {
            this.id = project.getId();
            this.name = project.getName();
            this.description = project.getDescription();
        }
    }

}
