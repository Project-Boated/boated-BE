package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class ProjectAccessDeniedException extends BusinessException {
    public ProjectAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
