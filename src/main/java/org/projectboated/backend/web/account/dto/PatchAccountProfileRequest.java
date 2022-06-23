package org.projectboated.backend.web.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchAccountProfileRequest {

    private String nickname;

    private String originalPassword;

    private String newPassword;

    private MultipartFile profileImageFile;
}
