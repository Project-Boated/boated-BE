package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class ProjectDeleteAccessDeniedException extends BusinessException {

    public ProjectDeleteAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
