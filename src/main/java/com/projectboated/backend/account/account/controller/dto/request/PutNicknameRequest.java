package com.projectboated.backend.account.account.controller.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class PutNicknameRequest {

    @NotBlank
    @Size(min = 2, max = 15)
    private String nickname;

    @Builder
    public PutNicknameRequest(String nickname) {
        this.nickname = nickname;
    }

}
