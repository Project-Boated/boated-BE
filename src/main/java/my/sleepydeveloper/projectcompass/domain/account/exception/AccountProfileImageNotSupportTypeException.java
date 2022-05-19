package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountProfileImageNotSupportTypeException extends BusinessException {

    public AccountProfileImageNotSupportTypeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
