package my.sleepydeveloper.projectcompass.web.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetMyCrewProjectResponse {

    private List<ProjectResponse> projects;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectResponse {

        private Long id;
        private String name;
        private String description;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadline;

        public ProjectResponse(Project project) {
            this.id = project.getId();
            this.name = project.getName();
            this.description = project.getDescription();
            this.deadline = project.getDeadline();
        }
    }

}
