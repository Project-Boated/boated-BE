package com.projectboated.backend.project.project.controller.dto.common;

import com.projectboated.backend.account.account.entity.Account;
import lombok.Getter;

@Getter
public class ProjectCaptainResponse {
    private Long id;
    private String nickname;

    public ProjectCaptainResponse(Account captain) {
        this.id = captain.getId();
        this.nickname = captain.getNickname();
    }
}