package my.sleepydeveloper.projectcompass.web.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {

    private String nickname;

    private String originalPassword;

    private String password;

    private String profileImageUrl;
}
