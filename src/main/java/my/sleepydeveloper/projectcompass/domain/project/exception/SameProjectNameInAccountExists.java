package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class SameProjectNameInAccountExists extends BusinessException {
    public SameProjectNameInAccountExists(ErrorCode errorCode) {
        super(errorCode);
    }
}
