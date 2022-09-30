package com.projectboated.backend.account.account.controller.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@Getter
@Setter
public class PatchAccountProfileRequest {

    @Size(min = 2, max = 15)
    private String nickname;

    private String originalPassword;

    @Size(min = 10, max = 20)
    private String newPassword;

    private MultipartFile profileImageFile;

}
