package com.projectboated.backend.task.task.controller.dto.common;

import com.projectboated.backend.account.account.entity.Account;
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
