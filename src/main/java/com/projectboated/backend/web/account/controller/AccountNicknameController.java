package com.projectboated.backend.web.account.controller;

import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.domain.account.service.AccountNicknameService;
import com.projectboated.backend.web.account.dto.PutNicknameRequest;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.web.account.dto.ValidationNicknameUniqueRequest;
import com.projectboated.backend.web.account.dto.ValidationNicknameUniqueResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account/profile/nickname")
public class AccountNicknameController {

    private final AccountNicknameService nicknameService;

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void putNickname(
            @Validated @RequestBody PutNicknameRequest request,
            @AuthenticationPrincipal Account account) {
        nicknameService.updateNickname(account, request.getNickname());
    }

    @PostMapping(value = "/unique-validation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ValidationNicknameUniqueResponse validationNicknameUnique(
            @Validated @RequestBody ValidationNicknameUniqueRequest request) {
        return new ValidationNicknameUniqueResponse(nicknameService.isNotSameAndExistsNickname(request.getNickname()));
    }

}