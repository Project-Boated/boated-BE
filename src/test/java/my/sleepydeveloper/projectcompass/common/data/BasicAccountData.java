package my.sleepydeveloper.projectcompass.common.data;

import my.sleepydeveloper.projectcompass.domain.account.entity.Role;

import java.util.Set;

public class BasicAccountData {

    public final static Long ACCOUNT_ID = 123L;
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
    public final static String NICKNAME = "nickname";
    public final static String PROFILE_IMAGE_URL = "profileImageUrl";
    public final static Set<Role> ROLES = Set.of(Role.USER);
}
