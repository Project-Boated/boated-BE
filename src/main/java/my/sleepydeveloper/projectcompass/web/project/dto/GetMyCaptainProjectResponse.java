package my.sleepydeveloper.projectcompass.web.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;

import java.time.LocalDateTime;
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

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadline;

        private ProjectCaptain captain;

        private List<ProjectCrew> crews;

        private boolean isTerminated;

        public ProjectResponse(Project project, List<Account> crews) {
            this.id = project.getId();
            this.name = project.getName();
            this.description = project.getDescription();
            this.deadline = project.getDeadline();
            this.captain = new ProjectCaptain(project.getCaptain());
            this.crews = crews.stream().map(ProjectCrew::new).toList();
            this.isTerminated = project.isTerminated();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectCaptain {
        private Long id;
        private String nickname;

        public ProjectCaptain(Account captain) {
            this.id = captain.getId();
            this.nickname = captain.getNickname();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectCrew {
        private Long id;
        private String nickname;

        public ProjectCrew(Account crew) {
            this.id = crew.getId();
            this.nickname = crew.getNickname();
        }
    }

}
