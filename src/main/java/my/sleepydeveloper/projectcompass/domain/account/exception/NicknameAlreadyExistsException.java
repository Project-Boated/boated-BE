package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class NicknameAlreadyExistsException extends BaseBusinessException {
    public NicknameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
