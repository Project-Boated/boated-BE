package com.projectboated.backend.task.taskfile.controller;

import com.projectboated.backend.infra.aws.AwsS3Service;
import com.projectboated.backend.task.taskfile.controller.dto.UploadTaskFileRequest;
import com.projectboated.backend.task.taskfile.controller.dto.UploadTaskFileResponse;
import com.projectboated.backend.task.taskfile.entity.TaskFile;
import com.projectboated.backend.task.taskfile.service.TaskFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/files")
public class TaskFileController {

    private final TaskFileService taskFileService;
    private final AwsS3Service awsS3Service;

    @PostMapping
    public UploadTaskFileResponse uploadTaskFile(@PathVariable Long projectId,
                                                 @PathVariable Long taskId,
                                                 @ModelAttribute @Validated UploadTaskFileRequest request
    ) {
        TaskFile taskFile = taskFileService.uploadFile(projectId, taskId, request.getFile());
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
