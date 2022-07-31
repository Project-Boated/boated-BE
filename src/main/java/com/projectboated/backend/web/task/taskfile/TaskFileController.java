package com.projectboated.backend.web.task.taskfile;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.task.taskfile.service.TaskFileService;
import com.projectboated.backend.web.task.taskfile.dto.UploadTaskFileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/files")
public class TaskFileController {

    private final TaskFileService taskFileService;

    @PostMapping
    public void uploadTaskFile(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @ModelAttribute @Validated UploadTaskFileRequest request
            ) {
        taskFileService.uploadFile(account.getId(), projectId, taskId, request.getFile());
    }


}
