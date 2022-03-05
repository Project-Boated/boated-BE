package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.common.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;

public class SameProjectNameInAccountExists extends BaseBusinessException {
    public SameProjectNameInAccountExists(ErrorCode errorCode) {
        super(errorCode);
    }
}
