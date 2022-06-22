package my.sleepydeveloper.projectcompass.web.account.controller;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountNicknameService;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.web.account.dto.PutNicknameRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.ValidationNicknameUniqueRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.ValidationNicknameUniqueResponse;
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