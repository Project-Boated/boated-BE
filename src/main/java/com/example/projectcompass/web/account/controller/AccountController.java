package com.example.projectcompass.web.account.controller;

import com.example.projectcompass.domain.account.entity.Account;
import com.example.projectcompass.domain.account.service.AccountService;
import com.example.projectcompass.security.dto.IdDto;
import com.example.projectcompass.security.dto.UsernamePasswordDto;
import com.example.projectcompass.web.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.ValidationException;

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
