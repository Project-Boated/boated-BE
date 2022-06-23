package org.projectboated.backend.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.account.exception.AccountNicknameAlreadyExistsException;
import org.projectboated.backend.domain.account.exception.AccountNotFoundException;
import org.projectboated.backend.domain.account.repository.AccountRepository;
import org.projectboated.backend.domain.exception.ErrorCode;
import org.projectboated.backend.utils.StringUtils;
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

        if (!StringUtils.equalsOrNull(account.getNickname(), nickname) &&
                accountRepository.existsByNickname(nickname)) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }

        findAccount.updateNickname(nickname);
    }

    public boolean isNotSameAndExistsNickname(String nickname) {
        return Objects.nonNull(nickname) && accountRepository.existsByNickname(nickname);
    }

}
