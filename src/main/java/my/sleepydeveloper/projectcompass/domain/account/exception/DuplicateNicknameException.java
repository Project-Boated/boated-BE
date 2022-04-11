package my.sleepydeveloper.projectcompass.domain.account.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class DuplicateNicknameException extends DefaultBusinessException {
    public DuplicateNicknameException(ErrorCode errorCode) {
        super(errorCode);
    }
}
