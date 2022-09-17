package com.projectboated.backend.account.account.controller;

import com.projectboated.backend.account.account.controller.dto.request.PutNicknameRequest;
import com.projectboated.backend.account.account.controller.dto.request.ValidationNicknameUniqueRequest;
import com.projectboated.backend.account.account.controller.dto.response.ValidationNicknameUniqueResponse;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.service.AccountNicknameService;
import lombok.RequiredArgsConstructor;
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
            @RequestBody @Validated PutNicknameRequest request,
            @AuthenticationPrincipal Account account) {
        nicknameService.updateNickname(account.getId(), request.getNickname());
    }

    @PostMapping(value = "/unique-validation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ValidationNicknameUniqueResponse validationNicknameUnique(
            @Validated @RequestBody ValidationNicknameUniqueRequest request) {
        return new ValidationNicknameUniqueResponse(nicknameService.existsByNickname(request.getNickname()));
    }

}
