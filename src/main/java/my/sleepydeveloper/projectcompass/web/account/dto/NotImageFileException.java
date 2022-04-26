package my.sleepydeveloper.projectcompass.web.account.dto;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class NotImageFileException extends BusinessException {
    public NotImageFileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
