package com.projectboated.backend.web.project.dto.response;

import com.projectboated.backend.domain.account.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrewResponse {

    private Long id;
    private String username;
    private String nickname;
    private String profileImageUrl;

    public CrewResponse(Account account, String profileImageUrl) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.nickname = account.getNickname();
        this.profileImageUrl = profileImageUrl;
    }
}
