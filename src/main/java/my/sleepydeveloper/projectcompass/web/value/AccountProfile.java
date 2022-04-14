package my.sleepydeveloper.projectcompass.web.value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountProfile {

    private String username;

    private String nickname;

    private String role;

    public AccountProfile(Account account) {
        this.username = account.getUsername();
        this.nickname = account.getNickname();
        this.role = account.getRole();
    }
}
