package com.projectboated.backend.web.task.taskfile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter @Setter
public class UploadTaskFileRequest {

    @NotNull
    MultipartFile file;

}
