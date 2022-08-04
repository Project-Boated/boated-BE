package com.projectboated.backend.web.task.task.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskAssignedFileResponse {

    private Long id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Builder
    public TaskAssignedFileResponse(UploadFile uploadFile) {
        this.id = uploadFile.getId();
        this.name = uploadFile.getOriginalFileName() + "." + uploadFile.getExt();
        this.createdDate = uploadFile.getCreatedDate();
    }
}
