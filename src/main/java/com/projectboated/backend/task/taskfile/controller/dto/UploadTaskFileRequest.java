package com.projectboated.backend.task.taskfile.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class UploadTaskFileRequest {

    @NotNull
    MultipartFile file;

}
