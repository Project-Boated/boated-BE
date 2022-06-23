package org.projectboated.backend.web.account.controller;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.account.service.AccountNicknameService;
import org.projectboated.backend.web.account.dto.PutNicknameRequest;
import org.projectboated.backend.web.account.dto.ValidationNicknameUniqueRequest;
import org.projectboated.backend.web.account.dto.ValidationNicknameUniqueResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account/profile/nickname")
public class AccountNicknameController {

    private final AccountNicknameService nicknameService;

    @PutMapping
    public void putNickname(
            @Validated @RequestBody PutNicknameRequest request,
            @AuthenticationPrincipal Account account) {
        nicknameService.updateNickname(account, request.getNickname());
    }

    @PostMapping("/unique-validation")
    public ValidationNicknameUniqueResponse validationNicknameUnique(
            @Validated @RequestBody ValidationNicknameUniqueRequest request) {
        return new ValidationNicknameUniqueResponse(nicknameService.isNotSameAndExistsNickname(request.getNickname()));
    }

}
