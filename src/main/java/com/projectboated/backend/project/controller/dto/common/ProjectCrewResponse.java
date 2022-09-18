package com.projectboated.backend.project.controller.dto.common;

import com.projectboated.backend.account.account.entity.Account;
import lombok.Getter;

@Getter
public class ProjectCrewResponse {
    private Long id;
    private String nickname;

    public ProjectCrewResponse(Account account) {
        this.id = account.getId();
        this.nickname = account.getNickname();
    }

}
