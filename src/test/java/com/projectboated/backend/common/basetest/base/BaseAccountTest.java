package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.account.account.entity.Account;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataAccount.ROLES;

public class BaseAccountTest extends BaseBasicTest {

    protected Account createAccount(Long id) {
        Account account = Account.builder()
                .id(id)
                .build();
        return account;
    }

    protected Account createDefaultAccount() {
        return Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .roles(ROLES)
                .build();
    }

    protected Account createDefaultAccount2() {
        return Account.builder()
                .username(USERNAME2)
                .password(PASSWORD2)
                .nickname(NICKNAME2)
                .roles(ROLES)
                .build();
    }

}
