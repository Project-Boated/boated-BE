package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class ProjectNotFoundException extends BaseBusinessException {
    public ProjectNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
