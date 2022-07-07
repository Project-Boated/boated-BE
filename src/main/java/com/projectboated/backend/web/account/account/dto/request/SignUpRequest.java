package com.projectboated.backend.web.account.account.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank
    @Size(min = 2, max = 15)
    private String username;

    @NotBlank
    @Size(min = 10, max = 15)
    private String password;

    @NotBlank
    @Size(min = 2, max=15)
    private String nickname;

    private String profileImageUrl;

    @Builder
    public SignUpRequest(String username, String password, String nickname, String profileImageUrl) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
