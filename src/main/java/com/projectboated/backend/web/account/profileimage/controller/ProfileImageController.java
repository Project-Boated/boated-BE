package com.projectboated.backend.web.account.profileimage.controller;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.service.exception.AccountProfileImageFileNotExist;
import com.projectboated.backend.infra.aws.AwsS3ProfileImageService;
import com.projectboated.backend.domain.account.profileimage.service.ProfileImageService;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.infra.aws.AwsS3File;
import com.projectboated.backend.web.account.account.exception.NotImageFileException;
import com.projectboated.backend.web.account.profileimage.dto.PostAccountProfileImageRequest;
import com.projectboated.backend.web.account.profileimage.dto.PostAccountProfileImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MimeType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account/profile/profile-image")
public class ProfileImageController {

    private final ProfileImageService profileImageService;
    private final AwsS3ProfileImageService awsS3ProfileImageService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostAccountProfileImageResponse postAccountProfileImage(@AuthenticationPrincipal Account account,
                                                                   @ModelAttribute @Validated PostAccountProfileImageRequest request,
                                                                   HttpServletRequest httpRequest) {

        MultipartFile file = request.getFile();
        if (file.isEmpty()) {
            throw new AccountProfileImageFileNotExist(ErrorCode.ACCOUNT_PROFILE_IMAGE_FILE_NOT_EXIST);
        }

        MimeType mimeType = MimeType.valueOf(file.getContentType());
        if (!mimeType.getType().equals("image")) {
            throw new NotImageFileException(ErrorCode.COMMON_NOT_IMAGE_FILE_EXCEPTION);
        }

        profileImageService.updateProfileImage(account.getId(), file);

        return new PostAccountProfileImageResponse(profileImageService.getProfileUrl(account.getId(), httpRequest.getHeader("HOST"), request.isProxy()));
    }

    @GetMapping
    public ResponseEntity<byte[]> getAccountProfileImage(@AuthenticationPrincipal Account account) {
        AwsS3File awsS3File = awsS3ProfileImageService.getProfileImageBytes(account);

        return ResponseEntity.
                ok()
                .contentType(MediaType.valueOf(awsS3File.getMediaType()))
                .contentLength(awsS3File.getBytes().length)
//                .header("Content-Disposition", "filename=\""+ awsS3File.getFileName() +"\"")
                .body(awsS3File.getBytes());
    }

    @DeleteMapping
    public void deleteAccountProfileImage(@AuthenticationPrincipal Account account) {
        profileImageService.deleteProfileImageFile(account);
    }

}
