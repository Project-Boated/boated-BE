package com.projectboated.backend.account.account.controller.dto.response;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GetAccountProfileResponse {

    private String username;
    private String nickname;
    private String profileImageUrl;
    private List<String> roles;

    @Builder
    public GetAccountProfileResponse(Account account, String profileImageUrl) {
        this.username = account.getUsername();
        this.nickname = account.getNickname();
        this.profileImageUrl = profileImageUrl;
        this.roles = account.getRoles().stream().map(Role::getName).toList();
    }

}
