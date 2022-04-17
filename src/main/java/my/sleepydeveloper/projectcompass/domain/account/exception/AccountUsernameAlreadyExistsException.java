package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class AccountUsernameAlreadyExistsException extends BusinessException {
    public AccountUsernameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
