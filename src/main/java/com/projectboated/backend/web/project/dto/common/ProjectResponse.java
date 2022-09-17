package com.projectboated.backend.web.project.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    private ProjectCaptainResponse captain;

    private List<ProjectCrewResponse> crews;

    private boolean isTerminated;

    private Long dday;

    private Long totalDay;

    @Builder
    public ProjectResponse(Project project, ProjectCaptainResponse projectCaptainResponse, List<Account> crews) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.deadline = project.getDeadline();
        this.captain = new ProjectCaptainResponse(project.getCaptain());
        this.isTerminated = project.isTerminated();
        this.dday = project.getDeadline() != null ? ChronoUnit.DAYS.between(deadline.toLocalDate(), LocalDate.now()) : null;
        this.totalDay = project.getDeadline() != null ? ChronoUnit.DAYS.between(project.getCreatedDate().toLocalDate(), deadline.toLocalDate()) : null;

        this.captain = projectCaptainResponse;
        this.crews = crews.stream().map(ProjectCrewResponse::new).toList();
    }
}