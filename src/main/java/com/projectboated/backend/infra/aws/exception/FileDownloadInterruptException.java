package com.projectboated.backend.infra.aws.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class FileDownloadInterruptException extends BusinessException {
    public FileDownloadInterruptException(Throwable cause) {
        super(ErrorCode.FILE_DOWNLOAD_INTERRUPT, cause);
    }
}
