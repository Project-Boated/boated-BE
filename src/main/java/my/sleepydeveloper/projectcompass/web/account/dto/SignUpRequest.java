package my.sleepydeveloper.projectcompass.web.account.dto;

import my.sleepydeveloper.projectcompass.security.dto.UsernamePasswordDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    String nickname;

    String profileImageUrl;

    public SignUpRequest(String username, String password, String nickname, String profileImageUrl) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
