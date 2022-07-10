package com.projectboated.backend.common.data;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.entity.Role;
import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.account.profileimage.entity.UrlProfileImage;

import java.util.Set;

public abstract class BasicDataAccount {

    public final static Long ACCOUNT_ID = 123L;
    public final static String USERNAME = "testUsername";
    public final static String PASSWORD = "testPassword1";
    public final static String NICKNAME = "testNickname";
    public final static String PROFILE_IMAGE_URL = "testProfileImageUrl";
    public final static ProfileImage URL_PROFILE_IMAGE = new UrlProfileImage(PROFILE_IMAGE_URL);
    public final static Set<Role> ROLES = Set.of(Role.USER);
    public final static Account ACCOUNT = Account.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .nickname(NICKNAME)
            .roles(ROLES)
            .build();

    public final static Long ACCOUNT_ID2 = 124L;
    public final static String USERNAME2 = "testUsername2";
    public final static String PASSWORD2 = "testPassword2";
    public final static String NICKNAME2 = "testNickname2";
    public final static String PROFILE_IMAGE_URL2 = "testProfileImageUrl2";
    public final static ProfileImage URL_PROFILE_IMAGE2 = new UrlProfileImage(PROFILE_IMAGE_URL2);
    public final static Account ACCOUNT2 = Account.builder()
            .username(USERNAME2)
            .password(PASSWORD2)
            .nickname(NICKNAME2)
            .roles(ROLES)
            .build();
}
