package com.projectboated.backend.web.task.taskfile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UploadTaskFileResponse {

    private Long id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    public UploadTaskFileResponse(TaskFile taskFile) {
        this.id = taskFile.getId();
        this.name = taskFile.getUploadFile().getFullOriginalFileName();
        this.createdDate = taskFile.getCreatedDate();
    }
}
