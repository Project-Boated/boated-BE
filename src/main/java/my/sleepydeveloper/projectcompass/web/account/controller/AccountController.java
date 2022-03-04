package my.sleepydeveloper.projectcompass.web.account.controller;

import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.domain.account.service.dto.AccountUpdateCondition;
import my.sleepydeveloper.projectcompass.domain.account.value.AccountProfile;
import my.sleepydeveloper.projectcompass.security.dto.IdDto;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountProfileResponseDto;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountUpdateRequest;
import my.sleepydeveloper.projectcompass.web.account.exception.AccountUpdateAccessDenied;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/sign-up")
    public ResponseEntity<IdDto> signUp(@RequestBody @Validated AccountDto accountDto) {

        String username = accountDto.getUsername();
        String password = accountDto.getPassword();
        String nickname = accountDto.getNickname();
        String role = "ROLE_USER";

        Account savedAccount = accountService.save(
                new Account(username, passwordEncoder.encode(password), nickname, role));

        return ResponseEntity.created(URI.create("/api/account/profile"+savedAccount.getId())).body(new IdDto(savedAccount.getId()));
    }

    @GetMapping("/profile")
    public ResponseEntity<AccountProfileResponseDto> getAccountProfile(@AuthenticationPrincipal Account account) {
        AccountProfile accountProfile = accountService.findProfileByAccount(account);

        return ResponseEntity.ok(new AccountProfileResponseDto(accountProfile));
    }

    @PatchMapping("/profile/{accountId}")
    public ResponseEntity<IdDto> updateAccountProfile(@AuthenticationPrincipal Account account,
                                                   @PathVariable Long accountId,
                                                   @RequestBody @Validated AccountUpdateRequest accountUpdateRequest) {

        checkRightAccess(account, accountId);

        accountService.updateProfile(AccountUpdateCondition.builder()
                        .accountId(accountId)
                        .originalPassword(accountUpdateRequest.getOriginalPassword())
                        .nickname(accountUpdateRequest.getNickname())
                        .password(accountUpdateRequest.getPassword())
                .build());

        return ResponseEntity.ok(new IdDto(account.getId()));
    }

    private void checkRightAccess(Account account, Long accountId) {
        if (account.getId() != accountId) {
            throw new AccountUpdateAccessDenied(ErrorCode.ACCOUNT_UPDATE_ACCESS_DENIED);
        }
    }

    @DeleteMapping("/profile/{accountId}")
    public ResponseEntity<IdDto> deleteAccount(@AuthenticationPrincipal Account account,
                                               @PathVariable Long accountId,
                                               HttpSession session) {
        checkRightAccess(account, accountId);
        accountService.delete(account);
        session.invalidate();

        return ResponseEntity.ok(new IdDto(accountId));
    }
}
