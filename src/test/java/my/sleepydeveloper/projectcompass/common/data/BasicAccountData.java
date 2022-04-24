package my.sleepydeveloper.projectcompass.common.data;

import my.sleepydeveloper.projectcompass.domain.account.entity.Role;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;

import java.util.Set;

public class BasicAccountData {

    public final static Long ACCOUNT_ID = 123L;
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
    public final static String NICKNAME = "nickname";
    public final static String PROFILE_IMAGE_URL = "profile_image_url";
    public final static UploadFile PROFILE_IMAGE_FILE = new UploadFile(PROFILE_IMAGE_URL);
    public final static Set<Role> ROLES = Set.of(Role.USER);
}
