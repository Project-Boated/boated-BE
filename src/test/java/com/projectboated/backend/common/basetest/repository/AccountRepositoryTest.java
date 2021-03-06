package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataAccount.ROLES;

public class AccountRepositoryTest extends BaseRepositoryTest{

    @Autowired
    protected AccountRepository accountRepository;

    protected Account insertDefaultAccount() {
        return accountRepository.save(Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .roles(ROLES)
                .build());
    }

    protected Account insertDefaultAccount2() {
        return accountRepository.save(Account.builder()
                .username(USERNAME2)
                .password(PASSWORD2)
                .nickname(NICKNAME2)
                .roles(ROLES)
                .build());
    }

}
