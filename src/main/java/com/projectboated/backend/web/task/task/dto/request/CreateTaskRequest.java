package com.projectboated.backend.web.task.task.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateTaskRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;

    @Builder
    public CreateTaskRequest(String name, String description, LocalDateTime deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }
}
