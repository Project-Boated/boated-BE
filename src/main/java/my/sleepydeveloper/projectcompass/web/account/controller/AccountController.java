package my.sleepydeveloper.projectcompass.web.account.controller;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.domain.account.value.AccountProfile;
import my.sleepydeveloper.projectcompass.security.dto.IdDto;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountProfileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AccountProfileDto> getAccountProfile(@AuthenticationPrincipal Account account) {
        AccountProfile accountProfile = accountService.findAccountProfileByAccount(account);

        return ResponseEntity.ok(new AccountProfileDto(accountProfile));
    }
}
