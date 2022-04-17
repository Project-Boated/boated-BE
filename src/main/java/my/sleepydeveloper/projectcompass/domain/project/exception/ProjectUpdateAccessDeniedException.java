package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class ProjectUpdateAccessDeniedException extends BusinessException {
    public ProjectUpdateAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
