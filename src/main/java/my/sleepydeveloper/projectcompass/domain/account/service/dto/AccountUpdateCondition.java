package my.sleepydeveloper.projectcompass.domain.account.service.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateCondition {

    private Long accountId;
    private String nickname;
    private String originalPassword;
    private String password;
}
