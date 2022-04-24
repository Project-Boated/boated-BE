package my.sleepydeveloper.projectcompass.aws.exception;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class FileUploadInterruptException extends BusinessException {
    public FileUploadInterruptException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
