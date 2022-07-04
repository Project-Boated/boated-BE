package com.projectboated.backend.web.account.account.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    String nickname;

    String profileImageUrl;

    public SignUpRequest(String username, String password, String nickname, String profileImageUrl) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
