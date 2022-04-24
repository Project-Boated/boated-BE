package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountProfileImageFileNotExist extends BusinessException {
    public AccountProfileImageFileNotExist(ErrorCode errorCode) {
        super(errorCode);
    }

    public AccountProfileImageFileNotExist(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
