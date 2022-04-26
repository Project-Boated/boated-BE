package my.sleepydeveloper.projectcompass.domain.account.service.condition;

import lombok.*;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountUpdateCond {

    private String nickname;
    private String originalPassword;
    private String newPassword;
    private MultipartFile profileImageFile;
}
