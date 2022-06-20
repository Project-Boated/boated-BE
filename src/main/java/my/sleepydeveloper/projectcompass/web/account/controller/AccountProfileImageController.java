package my.sleepydeveloper.projectcompass.web.account.controller;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.aws.AwsS3File;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountProfileImageFileNotExist;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountProfileImageService;
import my.sleepydeveloper.projectcompass.domain.common.exception.CommonIOException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.UploadFileProfileImage;
import my.sleepydeveloper.projectcompass.domain.profileimage.service.AwsS3ProfileImageService;
import my.sleepydeveloper.projectcompass.domain.profileimage.service.ProfileImageService;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import my.sleepydeveloper.projectcompass.domain.uploadfile.service.UploadFileService;
import my.sleepydeveloper.projectcompass.web.account.dto.NotImageFileException;
import my.sleepydeveloper.projectcompass.web.account.dto.PostAccountProfileImageRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.PostAccountProfileImageResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MimeType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountProfileImageController {

    private final ProfileImageService profileImageService;
    private final AccountProfileImageService accountProfileImageService;
    private final AwsS3ProfileImageService awsS3ProfileImageService;


    @PostMapping("/api/account/profile/profile-image")
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
