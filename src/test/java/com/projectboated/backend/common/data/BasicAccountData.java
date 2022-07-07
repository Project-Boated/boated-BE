package com.projectboated.backend.common.data;

import com.projectboated.backend.domain.account.account.entity.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.account.profileimage.entity.UrlProfileImage;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicAccountData {

    public final static Long ACCOUNT_ID = 123L;
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password1234";
    public final static String NICKNAME = "nickname";
    public final static String PROFILE_IMAGE_URL = "profile_image_url";
    public final static ProfileImage PROFILE_IMAGE_FILE = new UrlProfileImage(PROFILE_IMAGE_URL);
    public final static Set<Role> ROLES = Set.of(Role.USER);
}
