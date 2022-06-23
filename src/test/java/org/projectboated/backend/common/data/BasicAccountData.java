package org.projectboated.backend.common.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.projectboated.backend.domain.account.entity.Role;
import org.projectboated.backend.domain.profileimage.entity.ProfileImage;
import org.projectboated.backend.domain.profileimage.entity.UrlProfileImage;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicAccountData {

    public final static Long ACCOUNT_ID = 123L;
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
    public final static String NICKNAME = "nickname";
    public final static String PROFILE_IMAGE_URL = "profile_image_url";
    public final static ProfileImage PROFILE_IMAGE_FILE = new UrlProfileImage(PROFILE_IMAGE_URL);
    public final static Set<Role> ROLES = Set.of(Role.USER);
}
