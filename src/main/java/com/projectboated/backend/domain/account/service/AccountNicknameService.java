package com.projectboated.backend.domain.account.service;

import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.account.exception.AccountNicknameAlreadyExistsException;
import com.projectboated.backend.domain.account.exception.AccountNotFoundException;
import com.projectboated.backend.domain.account.repository.AccountRepository;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountNicknameService {

    private final AccountRepository accountRepository;

    @Transactional
    public void updateNickname(Account account, String nickname) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!equalsOrNull(account.getNickname(), nickname) &&
                accountRepository.existsByNickname(nickname)) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }

        findAccount.updateNickname(nickname);
    }

    public boolean isNotSameAndExistsNickname(String nickname) {
        return Objects.nonNull(nickname) && accountRepository.existsByNickname(nickname);
    }

    public boolean equalsOrNull(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return true;
        }
        return s1.equals(s2);
    }

}
