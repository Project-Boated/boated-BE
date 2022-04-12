package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.DefaultBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class SameProjectNameInAccountExists extends DefaultBusinessException {
    public SameProjectNameInAccountExists(ErrorCode errorCode) {
        super(errorCode);
    }
}
