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
    public final static Account ACCOUNT = new Account(ACCOUNT_ID, USERNAME, PASSWORD, NICKNAME, URL_PROFILE_IMAGE, ROLES);
}
