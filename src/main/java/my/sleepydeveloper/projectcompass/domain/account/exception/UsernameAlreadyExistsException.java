package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.common.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;

public class UsernameAlreadyExistsException extends BaseBusinessException {
    public UsernameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
