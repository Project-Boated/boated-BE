package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class NicknameAlreadyExistsException extends DefaultBusinessException {
    public NicknameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
