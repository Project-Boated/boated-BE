package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.common.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;

public class DuplicateNicknameException extends BaseBusinessException {
    public DuplicateNicknameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
