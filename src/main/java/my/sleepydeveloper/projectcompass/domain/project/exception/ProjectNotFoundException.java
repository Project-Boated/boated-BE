package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.common.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.common.exception.ErrorCode;

public class ProjectNotFoundException extends BaseBusinessException {
    public ProjectNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
