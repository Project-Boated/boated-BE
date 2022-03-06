package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class SameProjectNameInAccountExists extends BaseBusinessException {
    public SameProjectNameInAccountExists(ErrorCode errorCode) {
        super(errorCode);
    }
}
