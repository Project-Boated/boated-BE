package my.sleepydeveloper.projectcompass.web.account.controller;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.Role;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountProfileImageService;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.domain.account.service.condition.AccountUpdateCond;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.UrlProfileImage;
import my.sleepydeveloper.projectcompass.web.account.dto.GetAccountProfileResponse;
import my.sleepydeveloper.projectcompass.web.account.dto.NotImageFileException;
import my.sleepydeveloper.projectcompass.web.account.dto.PatchAccountProfileRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.SignUpRequest;
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
    private final AccountProfileImageService profileImageService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Validated SignUpRequest accountDto) {
        String username = accountDto.getUsername();
        String password = accountDto.getPassword();
        String nickname = accountDto.getNickname();
        String profileImageUrl = accountDto.getProfileImageUrl();

        Account savedAccount = accountService.save(new Account(username, password, nickname, new UrlProfileImage(profileImageUrl), Set.of(Role.USER)));

        return ResponseEntity.created(URI.create("/api/account/profile" + savedAccount.getId())).build();
    }

    @GetMapping("/profile")
    public GetAccountProfileResponse getAccountProfile(@AuthenticationPrincipal Account account,
                                                       HttpServletRequest request,
                                                       @RequestParam(name = "isProxy", required = false) boolean isProxy) {
        Account findAccount = accountService.findById(account.getId());
        String profileUrl = profileImageService.getProfileUrl(account, request, isProxy);
        String nickname = findAccount.getNickname();
        String username = findAccount.getUsername();
        Set<Role> roles = findAccount.getRoles();

        return new GetAccountProfileResponse(username, nickname, profileUrl, roles.stream()
                .map(Role::getName)
                .toList());
    }

    @PatchMapping("/profile")
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
