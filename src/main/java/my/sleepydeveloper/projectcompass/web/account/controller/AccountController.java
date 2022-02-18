package my.sleepydeveloper.projectcompass.web.account.controller;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.security.dto.IdDto;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/sign-up")
    public ResponseEntity<IdDto> signUp(@RequestBody @Validated AccountDto accountDto) {

        String username = accountDto.getUsername();
        String password = accountDto.getPassword();
        String nickname = accountDto.getNickname();
        String role = "ROLE_USER";

        Account savedAccount = accountService.save(
                new Account(username, passwordEncoder.encode(password), nickname, role));

        return ResponseEntity.ok(new IdDto(savedAccount.getId()));
    }
}
