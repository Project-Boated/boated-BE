package com.projectboated.backend.web.project.dto.common;

import com.projectboated.backend.domain.account.account.entity.Account;
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