package com.projectboated.backend.web.task.task.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.web.task.task.dto.common.TaskAssignedAccountResponse;
import com.projectboated.backend.web.task.task.dto.common.TaskAssignedFileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetTaskResponse {

    private Long id;

    private String name;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    private Long laneId;

    private List<TaskAssignedAccountResponse> assignedAccounts;

    private List<TaskAssignedFileResponse> assignedFiles;

    @Builder
    public GetTaskResponse(Task task, List<Account> assignedAccounts, List<UploadFile> assignedUploadFile) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.deadline = task.getDeadline();
        this.laneId = task.getKanbanLane().getId();
        this.assignedAccounts = assignedAccounts.stream()
                .map(TaskAssignedAccountResponse::new)
                .toList();
        this.assignedFiles = assignedUploadFile.stream()
                .map(TaskAssignedFileResponse::new)
                .toList();
    }
}
