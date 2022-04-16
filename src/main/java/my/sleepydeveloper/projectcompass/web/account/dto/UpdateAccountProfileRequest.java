package my.sleepydeveloper.projectcompass.web.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountProfileRequest {

    private String nickname;

    private String originalPassword;

    private String newPassword;

    private String profileImageUrl;
}
