package com.projectboated.backend.web.account.account.controller;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.entity.Role;
import com.projectboated.backend.domain.account.account.service.AccountService;
import com.projectboated.backend.domain.account.account.service.condition.AccountUpdateCond;
import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.account.profileimage.entity.UrlProfileImage;
import com.projectboated.backend.domain.account.profileimage.service.AccountProfileImageService;
import com.projectboated.backend.domain.account.profileimage.service.ProfileImageService;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.web.account.account.dto.request.PatchAccountProfileRequest;
import com.projectboated.backend.web.account.account.dto.request.SignUpRequest;
import com.projectboated.backend.web.account.account.dto.response.GetAccountProfileResponse;
import com.projectboated.backend.web.account.account.exception.NotImageFileException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MimeType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountProfileImageService accountProfileImageService;
    private final ProfileImageService profileImageService;

    @PostMapping(value = "/api/account/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void signUp(@RequestBody @Validated SignUpRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String nickname = request.getNickname();
        String profileImageUrl = request.getProfileImageUrl();

        ProfileImage profileImage = profileImageService.save(new UrlProfileImage(profileImageUrl));

        accountService.save(new Account(username, password, nickname, profileImage, Set.of(Role.USER)));
    }

    @GetMapping("/api/account/profile")
    public GetAccountProfileResponse getAccountProfile(HttpServletRequest httpRequest,
                                                       @AuthenticationPrincipal Account account,
                                                       @RequestParam(name = "isProxy", required = false) boolean isProxy) {
        Account findAccount = accountService.findById(account.getId());

        return GetAccountProfileResponse.builder()
                .account(findAccount)
                .profileImageUrl(accountProfileImageService.getProfileUrl(findAccount, httpRequest, isProxy))
                .build();
    }

    @PatchMapping(value = "/api/account/profile")
    public void patchAccountProfile(
            @AuthenticationPrincipal Account account,
            @ModelAttribute @Validated PatchAccountProfileRequest request
    ) {
        if (request.getProfileImageFile() != null) {
            MimeType mimeType = MimeType.valueOf(Objects.requireNonNull(request.getProfileImageFile().getContentType()));
            if (!mimeType.getType().equals("image")) {
                throw new NotImageFileException(ErrorCode.COMMON_NOT_IMAGE_FILE_EXCEPTION);
            }
        }

        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .originalPassword(request.getOriginalPassword())
                .newPassword(request.getNewPassword())
                .nickname(request.getNickname())
                .profileImageFile(request.getProfileImageFile())
                .build();

        accountService.updateProfile(account, updateCondition);
    }

    @DeleteMapping("/api/account/profile")
    public void deleteAccount(@AuthenticationPrincipal Account account, HttpSession session) {
        accountService.delete(account);
        session.invalidate();
    }
}
