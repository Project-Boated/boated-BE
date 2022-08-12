package com.projectboated.backend.web.account.account.dto.request;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank
    @Size(min = 2, max = 15)
    private String username;

    @NotBlank
    @Size(min = 10, max = 15)
    private String password;

    @NotBlank
    @Size(min = 2, max=15)
    private String nickname;

    @Builder
    public SignUpRequest(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public Account toAccount() {
        return Account.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .roles(Set.of(Role.USER))
                .build();
    }
}
