package my.sleepydeveloper.projectcompass.domain.account.service.dto;

import lombok.*;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateCondition {

    private String nickname;
    private String originalPassword;
    private String password;
}
