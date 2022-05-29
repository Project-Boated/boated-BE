package my.sleepydeveloper.projectcompass.domain.account.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountNicknameAlreadyExistsException;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountNotFoundException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static my.sleepydeveloper.projectcompass.utils.StringUtils.equalsOrNull;

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

}
