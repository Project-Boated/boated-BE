package my.sleepydeveloper.projectcompass.common.data;

import my.sleepydeveloper.projectcompass.domain.account.entity.Role;
import my.sleepydeveloper.projectcompass.domain.uploadfile.UploadFile;

import java.io.File;
import java.util.Set;

public class BasicAccountData {

    public final static Long ACCOUNT_ID = 123L;
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
    public final static String NICKNAME = "nickname";
    public final static UploadFile PROFILE_UPLOAD_FILE = new UploadFile("original", "saved", "url");
    public final static Set<Role> ROLES = Set.of(Role.USER);
}
