package com.projectboated.backend.utils.base.base;

import com.projectboated.backend.account.account.entity.Account;

import static com.projectboated.backend.utils.data.BasicDataAccount.*;

public class BaseAccountTest extends BaseBasicTest {

    protected Account createAccount() {
        return Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .roles(ROLES)
                .build();
    }

    protected Account createAccount(String username, String nickname) {
        return Account.builder()
                .username(username)
                .password(PASSWORD)
                .nickname(nickname)
                .roles(ROLES)
                .build();
    }

    protected Account createAccount(Long id) {
        return Account.builder()
                .id(id)
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .roles(ROLES)
                .profileImageFile(URL_PROFILE_IMAGE)
                .build();
    }

}
