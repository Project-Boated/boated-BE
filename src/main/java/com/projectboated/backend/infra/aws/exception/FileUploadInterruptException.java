package com.projectboated.backend.infra.aws.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class FileUploadInterruptException extends BusinessException {
    public FileUploadInterruptException(Throwable cause) {
        super(ErrorCode.COMMON_IO_EXCEPTION, cause);
    }
}
