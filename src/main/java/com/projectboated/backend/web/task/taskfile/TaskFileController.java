package com.projectboated.backend.web.task.taskfile;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.task.taskfile.service.TaskFileService;
import com.projectboated.backend.infra.aws.AwsS3Service;
import com.projectboated.backend.web.task.taskfile.dto.UploadTaskFileRequest;
import com.projectboated.backend.web.task.taskfile.dto.UploadTaskFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/files")
public class TaskFileController {

    private final TaskFileService taskFileService;
    private final AwsS3Service awsS3Service;

    @PostMapping
    public UploadTaskFileResponse uploadTaskFile(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @ModelAttribute @Validated UploadTaskFileRequest request
            ) {
        TaskFile taskFile = taskFileService.uploadFile(account.getId(), projectId, taskId, request.getFile());
        return new UploadTaskFileResponse(taskFile);
    }

    @GetMapping("/{taskFileId}")
    public ResponseEntity<byte[]> getTaskFile(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long taskFileId
    ) {
        TaskFile taskFile = taskFileService.findById(projectId, taskFileId);

        byte[] body = awsS3Service.getBytes(taskFile.getKey());

        return ResponseEntity.
                ok()
                .contentType(MediaType.valueOf(taskFile.getUploadFile().getMediaType()))
                .contentLength(body.length)
                .body(body);
    }

    @DeleteMapping("/{taskFileId}")
    public void deleteTaskFile(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long taskFileId
    ) {
        taskFileService.delete(projectId, taskId, taskFileId);
    }


}
