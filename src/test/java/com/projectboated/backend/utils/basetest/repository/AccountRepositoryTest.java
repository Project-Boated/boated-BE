package com.projectboated.backend.utils.basetest.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountRepositoryTest extends BaseRepositoryTest {

    @Autowired
    protected AccountRepository accountRepository;

    protected Account insertAccount(String username, String nickname) {
        return accountRepository.save(createAccount(username, nickname));
    }

    protected Account insertAccount() {
        return accountRepository.save(createAccount());
    }

}
