package my.sleepydeveloper.projectcompass.web.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.sleepydeveloper.projectcompass.domain.account.value.AccountProfile;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountProfileResponseDto {

    private String username;
    private String nickname;
    private String role;

    public AccountProfileResponseDto(AccountProfile accountProfile) {
        this.username = accountProfile.getUsername();
        this.nickname = accountProfile.getNickname();
        this.role = accountProfile.getRole();
    }
}
