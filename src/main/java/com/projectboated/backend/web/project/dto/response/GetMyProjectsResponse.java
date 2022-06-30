package com.projectboated.backend.web.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.domain.account.entity.Account;
import lombok.*;
import com.projectboated.backend.domain.project.entity.Project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Builder
@Getter
public class GetMyProjectsResponse {

    private int page;
    private int size;
    private boolean hasNext;
    private List<ProjectResponse> content;

    public GetMyProjectsResponse(int page, int size, boolean hasNext, List<ProjectResponse> projectResponse) {
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
        this.content = projectResponse;
    }

    @Getter
    public static class ProjectResponse {

        private Long id;
        private String name;
        private String description;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadline;

        private ProjectCaptain captain;
        private List<ProjectCrew> crews;

        private boolean isTerminated;

        private Integer dday;

        private Integer totalDay;

        public ProjectResponse(Project project, List<Account> crews) {
            this.id = project.getId();
            this.name = project.getName();
            this.description = project.getDescription();
            this.deadline = project.getDeadline();
            this.captain = new ProjectCaptain(project.getCaptain());
            this.crews = crews.stream().map(ProjectCrew::new).toList();
            this.isTerminated = project.isTerminated();
            this.dday = project.getDeadline()!=null ? Period.between(deadline.toLocalDate(), LocalDate.now()).getDays() : null;
            this.totalDay = project.getDeadline()!=null ? Period.between(project.getCreatedDate().toLocalDate(), deadline.toLocalDate()).getDays() : null;
        }
    }

    @Getter
    public static class ProjectCaptain {
        private Long id;
        private String nickname;

        public ProjectCaptain(Account captain) {
            this.id = captain.getId();
            this.nickname = captain.getNickname();
        }
    }

    @Getter
    public static class ProjectCrew {
        private Long id;
        private String nickname;

        public ProjectCrew(Account crew) {
            this.id = crew.getId();
            this.nickname = crew.getNickname();
        }
    }


}
