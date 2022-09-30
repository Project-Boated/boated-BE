package com.projectboated.backend.account.account.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNicknameAlreadyExistsException;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountNicknameService {
    private final AccountRepository accountRepository;

    @Transactional
    public void updateNickname(Long accountId, String nickname) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        if (checkDuplicatedNickname(account, nickname)) {
            throw new AccountNicknameAlreadyExistsException();
        }

        account.changeNickname(nickname);
    }

    private boolean checkDuplicatedNickname(Account account, String nickname) {
        return account.getNickname() != null &&
                !account.getNickname().equals(nickname) &&
                accountRepository.existsByNickname(nickname);
    }

    public boolean existsByNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

}
