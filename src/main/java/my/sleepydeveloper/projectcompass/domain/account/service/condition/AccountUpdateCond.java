package my.sleepydeveloper.projectcompass.domain.account.service.condition;

import lombok.*;
import my.sleepydeveloper.projectcompass.domain.uploadfile.UploadFile;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateCond {

    private String nickname;
    private String originalPassword;
    private String newPassword;
    private UploadFile profileImageFile;
}
