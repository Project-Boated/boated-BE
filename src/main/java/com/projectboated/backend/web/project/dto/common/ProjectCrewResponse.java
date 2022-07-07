package com.projectboated.backend.web.project.dto.common;

import com.projectboated.backend.domain.account.account.entity.Account;

public class ProjectCrewResponse {
    private Long id;
    private String nickname;

    public ProjectCrewResponse(Account account) {
        this.id = account.getId();
        this.nickname = account.getNickname();
    }

}
