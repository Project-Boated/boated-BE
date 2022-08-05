package com.projectboated.backend.web.task.task.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.domain.task.task.entity.Task;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Getter
public class TaskResponse {

    private Long id;
    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;
    private Long dday;
    private int fileCount;
    private boolean isLike;

    private List<TaskAssignedAccountResponse> assignedAccounts;

    public TaskResponse(Task task, Map<Task, Boolean> taskLikeMap) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.deadline = task.getDeadline();
        this.dday = deadline!=null ? ChronoUnit.DAYS.between(deadline.toLocalDate(), LocalDate.now()) : null;
        this.fileCount = 0;
        this.assignedAccounts = task.getAssignedAccounts().stream()
                .map(at -> new TaskAssignedAccountResponse(at.getAccount()))
                .toList();
        this.isLike = taskLikeMap.get(task);
    }
}