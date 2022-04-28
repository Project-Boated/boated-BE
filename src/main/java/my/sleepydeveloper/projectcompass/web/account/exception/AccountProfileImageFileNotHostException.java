package my.sleepydeveloper.projectcompass.web.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountProfileImageFileNotHostException extends BusinessException {
    public AccountProfileImageFileNotHostException(ErrorCode errorCode) {
        super(errorCode);
    }
}
