package my.sleepydeveloper.projectcompass.domain.account.service.condition;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateCond {

    private String nickname;
    private String originalPassword;
    private String password;
}
