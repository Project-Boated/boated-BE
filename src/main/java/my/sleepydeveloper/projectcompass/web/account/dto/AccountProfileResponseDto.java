package my.sleepydeveloper.projectcompass.web.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountProfileResponseDto {

    private String username;
    private String nickname;
    private String profileUrl;
    private List<String> roles;

    public AccountProfileResponseDto(Account account) {
        this.username = account.getUsername();
        this.nickname = account.getNickname();
        this.profileUrl = account.getProfileUrl();
        this.roles = account.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }
}
