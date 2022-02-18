package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.common.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;

public class NicknameAlreadyExistsException extends BaseBusinessException {
    public NicknameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
