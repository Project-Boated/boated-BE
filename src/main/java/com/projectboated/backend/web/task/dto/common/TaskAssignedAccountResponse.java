package com.projectboated.backend.web.task.dto.common;

import com.projectboated.backend.domain.account.account.entity.Account;
import lombok.Getter;

@Getter
public class TaskAssignedAccountResponse {

    private Long id;
    private String nickname;

    public TaskAssignedAccountResponse(Account account) {
        this.id = account.getId();
        this.nickname = account.getNickname();
    }
}
