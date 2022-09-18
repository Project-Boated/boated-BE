package com.projectboated.backend.infra.aws.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class FileUploadInterruptException extends BusinessException {
    public FileUploadInterruptException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
