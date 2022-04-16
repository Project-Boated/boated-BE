package my.sleepydeveloper.projectcompass.web.account.controller;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.KakaoAccount;
import my.sleepydeveloper.projectcompass.domain.account.entity.Role;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.domain.account.service.condition.AccountUpdateCond;
import my.sleepydeveloper.projectcompass.security.dto.IdDto;
import my.sleepydeveloper.projectcompass.web.account.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Validated SignUpRequest accountDto) {
        String username = accountDto.getUsername();
        String password = accountDto.getPassword();
        String nickname = accountDto.getNickname();

        Account savedAccount = accountService.save(new Account(username, password, nickname, null, Set.of(Role.USER)));

        return ResponseEntity.created(URI.create("/api/account/profile" + savedAccount.getId())).build();
    }

    @GetMapping("/profile")
    public GetAccountProfileResponse getAccountProfile(@AuthenticationPrincipal Account account) {

        return new GetAccountProfileResponse(accountService.findById(account.getId()));
    }

    @PatchMapping("/profile")
    public void updateAccountProfile(@AuthenticationPrincipal Account account, @RequestBody @Validated AccountUpdateRequest accountUpdateRequest) {

        AccountUpdateCond updateCondition = AccountUpdateCond.builder()
                .originalPassword(accountUpdateRequest.getOriginalPassword())
                .nickname(accountUpdateRequest.getNickname())
                .password(accountUpdateRequest.getPassword())
                .profileImageUrl(accountUpdateRequest.getProfileImageUrl())
                .build();

        accountService.updateProfile(account, updateCondition);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<IdDto> deleteAccount(@AuthenticationPrincipal Account account, HttpSession session) {
        Long accountId = account.getId();

        accountService.delete(account);
        session.invalidate();

        return ResponseEntity.ok(new IdDto(accountId));
    }

    @PostMapping("/profile/nickname/unique-validation")
    public ResponseEntity<NicknameUniqueValidationResponse> nicknameUniqueValidation(@Validated @RequestBody NicknameUniqueValidationRequest request) {

        return ResponseEntity.ok(new NicknameUniqueValidationResponse(accountService.isExistsNickname(request.getNickname())));
    }

    @PutMapping("/profile/nickname")
    public void putNickname(@Validated @RequestBody PutNicknameRequest request, @AuthenticationPrincipal Account account) {
        accountService.updateNickname(account, request.getNickname());
    }
}
