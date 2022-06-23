package org.projectboated.backend.web.account.controller;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.aws.AwsS3File;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.account.exception.AccountProfileImageFileNotExist;
import org.projectboated.backend.domain.account.service.AccountProfileImageService;
import org.projectboated.backend.domain.exception.ErrorCode;
import org.projectboated.backend.domain.profileimage.service.AwsS3ProfileImageService;
import org.projectboated.backend.domain.profileimage.service.ProfileImageService;
import org.projectboated.backend.web.account.dto.NotImageFileException;
import org.projectboated.backend.web.account.dto.PostAccountProfileImageRequest;
import org.projectboated.backend.web.account.dto.PostAccountProfileImageResponse;
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
public class AccountProfileImageController {

    private final ProfileImageService profileImageService;
    private final AccountProfileImageService accountProfileImageService;
    private final AwsS3ProfileImageService awsS3ProfileImageService;


    @PostMapping(value = "/api/account/profile/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostAccountProfileImageResponse postAccountProfileImage(@AuthenticationPrincipal Account account,
                                                                   @Validated @ModelAttribute PostAccountProfileImageRequest postAccountProfileImageRequest,
                                                                   HttpServletRequest request) {

        MultipartFile file = postAccountProfileImageRequest.getFile();
        if(file.isEmpty()) {
            throw new AccountProfileImageFileNotExist(ErrorCode.ACCOUNT_PROFILE_IMAGE_FILE_NOT_EXIST);
        }

        MimeType mimeType = MimeType.valueOf(file.getContentType());
        if (!mimeType.getType().equals("image")) {
            throw new NotImageFileException(ErrorCode.COMMON_NOT_IMAGE_FILE_EXCEPTION);
        }

        profileImageService.updateProfileImage(account.getId(), file);

        return new PostAccountProfileImageResponse(accountProfileImageService.getProfileUrl(account, request, postAccountProfileImageRequest.isProxy()));
    }

    @GetMapping("/api/account/profile/profile-image")
    public ResponseEntity<byte[]> getAccountProfileImage(@AuthenticationPrincipal Account account) {
        accountProfileImageService.checkAccountProfileImageUploadFile(account);

        AwsS3File awsS3File = awsS3ProfileImageService.getProfileImageBytes(account);

        return ResponseEntity.
                ok()
                .contentType(MediaType.valueOf(awsS3File.getMediaType()))
                .contentLength(awsS3File.getBytes().length)
//                .header("Content-Disposition", "filename=\""+ awsS3File.getFileName() +"\"")
                .body(awsS3File.getBytes());
    }

    @DeleteMapping("/api/account/profile/profile-image")
    public void deleteAccountProfileImage(@AuthenticationPrincipal Account account) {
        accountProfileImageService.deleteProfileImageFile(account);
    }

}
