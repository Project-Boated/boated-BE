package com.projectboated.backend.task.task.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class AssignAccountTaskRequest {

    @NotBlank
    private String nickname;

    public AssignAccountTaskRequest(String nickname) {
        this.nickname = nickname;
    }
}
