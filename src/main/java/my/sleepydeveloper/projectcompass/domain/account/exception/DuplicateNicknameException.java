package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class DuplicateNicknameException extends BaseBusinessException {
    public DuplicateNicknameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
