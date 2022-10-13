package com.projectboated.backend.account.profileimage.controller;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.profileimage.controller.dto.PostAccountProfileImageRequest;
import com.projectboated.backend.account.profileimage.controller.dto.PostAccountProfileImageResponse;
import com.projectboated.backend.account.profileimage.service.ProfileImageService;
import com.projectboated.backend.common.utils.HttpUtils;
import com.projectboated.backend.infra.aws.AwsS3File;
import com.projectboated.backend.infra.aws.AwsS3ProfileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account/profile/profile-image")
public class ProfileImageController {

    private final HttpUtils httpUtils;
    private final ProfileImageService profileImageService;
    private final AwsS3ProfileImageService awsS3ProfileImageService;


    @PostMapping
    public PostAccountProfileImageResponse postAccountProfileImage(
            @AuthenticationPrincipal Account account,
            @ModelAttribute @Validated PostAccountProfileImageRequest request,
            HttpServletRequest httpRequest) {
        httpUtils.validateIsImageFile(request.getFile());
        profileImageService.updateProfileImage(account.getId(), request.getFile());

        return new PostAccountProfileImageResponse(
                profileImageService.getProfileUrl(account.getId(), httpUtils.getHostUrl(httpRequest, request.isProxy()))
        );
    }

    @GetMapping
    public ResponseEntity<byte[]> getAccountProfileImage(@AuthenticationPrincipal Account account) {
        AwsS3File awsS3File = awsS3ProfileImageService.getProfileImageBytes(account.getId());
        return ResponseEntity.
                ok()
                .contentType(MediaType.valueOf(awsS3File.getMediaType()))
                .contentLength(awsS3File.getBytes().length)
                .cacheControl(CacheControl.maxAge(31536000, TimeUnit.SECONDS))
                .body(awsS3File.getBytes());
    }

    @DeleteMapping
    public void deleteAccountProfileImage(@AuthenticationPrincipal Account account) {
        profileImageService.deleteProfileImageFile(account.getId());
    }

}
