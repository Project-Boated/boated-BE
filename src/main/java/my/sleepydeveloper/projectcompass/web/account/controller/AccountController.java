package my.sleepydeveloper.projectcompass.web.account.controller;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.aws.AwsS3File;
import my.sleepydeveloper.projectcompass.aws.AwsS3Service;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.Role;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.domain.account.service.condition.AccountUpdateCond;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import my.sleepydeveloper.projectcompass.domain.uploadfile.service.UploadFileService;
import my.sleepydeveloper.projectcompass.web.account.dto.*;
import my.sleepydeveloper.projectcompass.domain.common.exception.CommonIOException;
import my.sleepydeveloper.projectcompass.web.account.exception.AccountProfileImageFileNotHostException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MimeType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;
    private final UploadFileService uploadFileService;
    private final AwsS3Service awsS3Service;

    @Value("${cloud.aws.s3.baseUrl}")
    private String s3BaseUrl;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Validated SignUpRequest accountDto) {
        String username = accountDto.getUsername();
        String password = accountDto.getPassword();
        String nickname = accountDto.getNickname();
        String profileImageUrl = accountDto.getProfileImageUrl();

        Account savedAccount = accountService.save(new Account(username, password, nickname, new UploadFile(profileImageUrl), Set.of(Role.USER)));

        return ResponseEntity.created(URI.create("/api/account/profile" + savedAccount.getId())).build();
    }

    @GetMapping("/profile")
    public GetAccountProfileResponse getAccountProfile(@AuthenticationPrincipal Account account) {
        return new GetAccountProfileResponse(accountService.findById(account.getId()));
    }

    @PatchMapping("/profile")
    public void patchAccountProfile(
            @AuthenticationPrincipal Account account,
            @ModelAttribute @Validated PatchAccountProfileRequest patchAccountProfileRequest
    ) {

        MimeType mimeType = MimeType.valueOf(Objects.requireNonNull(patchAccountProfileRequest.getProfileImageFile().getContentType()));
        if(!mimeType.getType().equals("image")) {
            throw new NotImageFileException(ErrorCode.COMMON_NOT_IMAGE_FILE_EXCEPTION);
        }

        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .originalPassword(patchAccountProfileRequest.getOriginalPassword())
                .newPassword(patchAccountProfileRequest.getNewPassword())
                .nickname(patchAccountProfileRequest.getNickname())
                .profileImageFile(patchAccountProfileRequest.getProfileImageFile())
                .build();


        accountService.updateProfile(account, updateCondition);
    }

    @DeleteMapping("/profile")
    public void deleteAccount(@AuthenticationPrincipal Account account, HttpSession session) {
        accountService.delete(account);
        session.invalidate();
    }

    @PostMapping("/profile/nickname/unique-validation")
    public ValidationNicknameUniqueResponse validationNicknameUnique(
            @Validated @RequestBody ValidationNicknameUniqueRequest request) {

        return new ValidationNicknameUniqueResponse(accountService.isExistsNickname(request.getNickname()));
    }

    @PutMapping("/profile/nickname")
    public void putNickname(@Validated @RequestBody PutNicknameRequest request, @AuthenticationPrincipal Account account) {
        accountService.updateNickname(account, request.getNickname());
    }

    @PutMapping("/profile/profile-image")
    public void putAccountProfileImage(@AuthenticationPrincipal Account account,
                                           @RequestParam(name = "file") MultipartFile file) {
        MimeType mimeType = MimeType.valueOf(Objects.requireNonNull(file.getContentType()));
        if(!mimeType.getType().equals("image")) {
            throw new NotImageFileException(ErrorCode.COMMON_NOT_IMAGE_FILE_EXCEPTION);
        }

        try {
            UploadFile uploadFile = new UploadFile(file.getOriginalFilename(), UUID.randomUUID().toString(), file.getContentType(), "host");
            uploadFileService.save(uploadFile);

            awsS3Service.uploadProfileImage(account, file, uploadFile);
            accountService.updateProfileImage(account, uploadFile);
        } catch (IOException e) {
            throw new CommonIOException(ErrorCode.COMMON_IO_EXCEPTION, e);
        }
    }

    @GetMapping("/profile/profile-image")
    public ResponseEntity<byte[]> getAccountProfileImage(@AuthenticationPrincipal Account account) {
        if(!accountService.checkProfileImageHost(account)) {
            throw new AccountProfileImageFileNotHostException(ErrorCode.ACCOUNT_PROFILE_IMAGE_FILE_NOT_HOST);
        }

        AwsS3File awsS3File = awsS3Service.getProfileImageBytes(account);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf(awsS3File.getMediaType()));
        httpHeaders.setContentLength(awsS3File.getBytes().length);
        httpHeaders.setContentDispositionFormData("attachment", awsS3File.getFileName());

        return new ResponseEntity<>(awsS3File.getBytes(), httpHeaders, HttpStatus.OK);
    }

    @DeleteMapping("/profile/profile-image")
    public void deleteAccountProfileImage(@AuthenticationPrincipal Account account) {
        accountService.deleteProfileImageFile(account);
    }
}
