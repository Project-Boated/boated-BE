package my.sleepydeveloper.projectcompass.web.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.Role;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.UploadFileProfileImage;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountProfileResponse {

    private String username;
    private String nickname;
    private String profileImageUrl;
    private List<String> roles;
}
