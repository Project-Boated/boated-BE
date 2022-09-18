package com.projectboated.backend.task.task.controller.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.task.task.entity.Task;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
public class TaskNoLikeResponse {

    private Long id;
    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;
    private Long dday;
    private int fileCount;
    private List<TaskAssignedAccountResponse> assignedAccounts;

    public TaskNoLikeResponse(Task task, List<TaskAssignedAccountResponse> assignedAccounts) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.deadline = task.getDeadline();
        this.dday = deadline != null ? ChronoUnit.DAYS.between(deadline.toLocalDate(), LocalDate.now()) : null;
        this.fileCount = 0;
        this.assignedAccounts = assignedAccounts;
    }
}