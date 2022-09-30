package com.projectboated.backend.task.task.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.uploadfile.entity.UploadFile;
import com.projectboated.backend.task.task.controller.dto.common.TaskAssignedAccountResponse;
import com.projectboated.backend.task.task.controller.dto.common.TaskAssignedFileResponse;
import com.projectboated.backend.task.task.entity.Task;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetTaskResponse {

    private Long id;

    private String name;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    private String laneName;

    private List<TaskAssignedAccountResponse> assignedAccounts;

    private List<TaskAssignedFileResponse> assignedFiles;

    @Builder
    public GetTaskResponse(Task task, List<Account> assignedAccounts, List<UploadFile> assignedUploadFile) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.deadline = task.getDeadline();
        this.laneName = task.getKanbanLane().getName();
        this.assignedAccounts = assignedAccounts.stream()
                .map(TaskAssignedAccountResponse::new)
                .toList();
        this.assignedFiles = assignedUploadFile.stream()
                .map(TaskAssignedFileResponse::new)
                .toList();
    }
}
