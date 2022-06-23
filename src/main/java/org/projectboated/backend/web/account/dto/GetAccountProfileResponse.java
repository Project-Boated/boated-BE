package org.projectboated.backend.web.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountProfileResponse {

    private String username;
    private String nickname;
    private String profileImageUrl;
    private List<String> roles;
}
