package com.projectboated.backend.web.account.controller;

import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.domain.account.entity.Role;
import com.projectboated.backend.domain.account.service.AccountProfileImageService;
import com.projectboated.backend.domain.account.service.AccountService;
import com.projectboated.backend.domain.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.profileimage.entity.UrlProfileImage;
import com.projectboated.backend.web.account.dto.GetAccountProfileResponse;
import com.projectboated.backend.web.account.dto.NotImageFileException;
import com.projectboated.backend.web.account.dto.PatchAccountProfileRequest;
import com.projectboated.backend.web.account.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.account.service.condition.AccountUpdateCond;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.profileimage.service.ProfileImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MimeType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.Objects;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;
    private final AccountProfileImageService accountProfileImageService;
    private final ProfileImageService profileImageService;

    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signUp(@RequestBody @Validated SignUpRequest accountDto) {
        String username = accountDto.getUsername();
        String password = accountDto.getPassword();
        String nickname = accountDto.getNickname();
        String profileImageUrl = accountDto.getProfileImageUrl();

        ProfileImage profileImage = profileImageService.save(new UrlProfileImage(profileImageUrl));

        Account savedAccount = accountService.save(new Account(username, password, nickname, profileImage, Set.of(Role.USER)));

        return ResponseEntity.created(URI.create("/api/account/profile" + savedAccount.getId())).build();
    }

    @GetMapping("/profile")
    public GetAccountProfileResponse getAccountProfile(@AuthenticationPrincipal Account account,
                                                       HttpServletRequest request,
                                                       @RequestParam(name = "isProxy", required = false) boolean isProxy) {
        Account findAccount = accountService.findById(account.getId());
        String profileUrl = accountProfileImageService.getProfileUrl(account, request, isProxy);
        String nickname = findAccount.getNickname();
        String username = findAccount.getUsername();
        Set<Role> roles = findAccount.getRoles();

        return new GetAccountProfileResponse(username, nickname, profileUrl, roles.stream()
                .map(Role::getName)
                .toList());
    }

    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void patchAccountProfile(
            @AuthenticationPrincipal Account account,
            @ModelAttribute @Validated PatchAccountProfileRequest requestAttribute
    ) {

        if (requestAttribute.getProfileImageFile() != null) {
            MimeType mimeType = MimeType.valueOf(Objects.requireNonNull(requestAttribute.getProfileImageFile().getContentType()));
            if (!mimeType.getType().equals("image")) {
                throw new NotImageFileException(ErrorCode.COMMON_NOT_IMAGE_FILE_EXCEPTION);
            }
        }

        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .originalPassword(requestAttribute.getOriginalPassword())
                .newPassword(requestAttribute.getNewPassword())
                .nickname(requestAttribute.getNickname())
                .profileImageFile(requestAttribute.getProfileImageFile())
                .build();


        accountService.updateProfile(account, updateCondition);
    }

    @DeleteMapping("/profile")
    public void deleteAccount(@AuthenticationPrincipal Account account, HttpSession session) {
        accountService.delete(account);
        session.invalidate();
    }
}