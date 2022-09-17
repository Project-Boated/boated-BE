package com.projectboated.backend.account.account.controller;

import com.projectboated.backend.account.account.controller.dto.request.PatchAccountProfileRequest;
import com.projectboated.backend.account.account.controller.dto.request.SignUpRequest;
import com.projectboated.backend.account.account.controller.dto.response.GetAccountProfileResponse;
import com.projectboated.backend.account.account.controller.exception.NotImageFileException;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.service.AccountService;
import com.projectboated.backend.account.account.service.condition.AccountUpdateCond;
import com.projectboated.backend.account.profileimage.service.ProfileImageService;
import com.projectboated.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MimeType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;
    private final ProfileImageService profileImageService;

    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void signUp(@RequestBody @Validated SignUpRequest request) {
        accountService.save(request.toAccount());
    }

    @GetMapping("/profile")
    public GetAccountProfileResponse getAccountProfile(HttpServletRequest httpRequest,
                                                       @AuthenticationPrincipal Account account,
                                                       @RequestParam(name = "isProxy", required = false) boolean isProxy) {
        return GetAccountProfileResponse.builder()
                .account(accountService.findById(account.getId()))
                .profileImageUrl(profileImageService.getProfileUrl(account.getId(), httpRequest.getHeader("HOST"), isProxy))
                .build();
    }

    @PatchMapping(value = "/profile")
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

        accountService.updateProfile(account.getId(), updateCondition);
    }

    @DeleteMapping("/profile")
    public void deleteAccount(@AuthenticationPrincipal Account account, HttpSession session) {
        accountService.delete(account.getId());
        session.invalidate();
    }

}
