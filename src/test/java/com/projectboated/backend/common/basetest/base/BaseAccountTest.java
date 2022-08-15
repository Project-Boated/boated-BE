package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.account.profileimage.entity.UrlProfileImage;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataAccount.ROLES;

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
