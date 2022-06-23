package org.projectboated.backend.aws.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class FileUploadInterruptException extends BusinessException {
    public FileUploadInterruptException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
