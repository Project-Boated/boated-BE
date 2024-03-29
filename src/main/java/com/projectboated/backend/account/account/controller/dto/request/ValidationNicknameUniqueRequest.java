package com.projectboated.backend.account.account.controller.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ValidationNicknameUniqueRequest {

    @NotBlank
    private String nickname;

    @Builder
    public ValidationNicknameUniqueRequest(String nickname) {
        this.nickname = nickname;
    }

}
