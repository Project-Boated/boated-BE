package org.projectboated.backend.web.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.project.entity.Project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetProjectResponse {

    private Long id;
    private String name;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    private ProjectCaptain captain;

    private boolean isTerminated;

    private Integer dday;

    private Integer totalDay;

    public GetProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.deadline = project.getDeadline();
        this.captain = new ProjectCaptain(project.getCaptain());
        this.isTerminated = project.isTerminated();
        this.dday = project.getDeadline()!=null ? Period.between(deadline.toLocalDate(), LocalDate.now()).getDays() : null;
        this.totalDay = project.getDeadline()!=null ? Period.between(project.getCreatedDate().toLocalDate(), deadline.toLocalDate()).getDays() : null;
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

}
