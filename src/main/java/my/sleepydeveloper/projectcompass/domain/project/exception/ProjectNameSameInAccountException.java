package my.sleepydeveloper.projectcompass.domain.project.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class ProjectNameSameInAccountException extends BusinessException {
    public ProjectNameSameInAccountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
