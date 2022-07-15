package com.projectboated.backend.domain.account.account.service.condition;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class AccountUpdateCond {

    private String nickname;
    private String originalPassword;
    private String newPassword;
    private MultipartFile profileImageFile;

    @Builder
    public AccountUpdateCond(String nickname, String originalPassword, String newPassword, MultipartFile profileImageFile) {
        this.nickname = nickname;
        this.originalPassword = originalPassword;
        this.newPassword = newPassword;
        this.profileImageFile = profileImageFile;
    }
}
