package com.projectboated.backend.project.controller.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.project.entity.Project;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class ProjectPlainResponse {

    private Long id;
    private String name;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    private boolean isTerminated;

    private Long dday;

    private Long totalDay;

    @Builder
    public ProjectPlainResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.deadline = project.getDeadline();
        this.isTerminated = project.isTerminated();
        this.dday = project.getDeadline() != null ? ChronoUnit.DAYS.between(deadline.toLocalDate(), LocalDate.now()) : null;
        this.totalDay = project.getDeadline() != null ? ChronoUnit.DAYS.between(project.getCreatedDate().toLocalDate(), deadline.toLocalDate()) : null;
    }
}