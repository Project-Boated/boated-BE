package com.projectboated.backend.web.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.web.project.dto.common.ProjectCaptainResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class GetProjectResponse {

    private Long id;
    private String name;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    private ProjectCaptainResponse captain;

    private boolean isTerminated;

    private Long dday;

    private Long totalDay;

    private long taskSize;

    private Long totalFileSize;

    @Builder
    public GetProjectResponse(Project project, long taskSize, ProjectCaptainResponse projectCaptainResponse, long totalFileSize) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.deadline = project.getDeadline();
        this.isTerminated = project.isTerminated();
        this.dday = project.getDeadline()!=null ? ChronoUnit.DAYS.between(deadline.toLocalDate(), LocalDate.now()) : null;
        this.totalDay = project.getDeadline()!=null ? ChronoUnit.DAYS.between(project.getCreatedDate().toLocalDate(), deadline.toLocalDate()) : null;

        this.captain = projectCaptainResponse;
        this.taskSize = taskSize;
        this.totalFileSize = totalFileSize;
    }

}
