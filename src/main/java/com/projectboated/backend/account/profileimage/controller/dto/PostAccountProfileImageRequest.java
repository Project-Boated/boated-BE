package com.projectboated.backend.account.profileimage.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PostAccountProfileImageRequest {

    @NotNull
    MultipartFile file;

    boolean isProxy;
}
