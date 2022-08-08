package com.projectboated.backend.infra.aws.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

import java.io.IOException;

public class FileDownloadInterruptException extends BusinessException {
    public FileDownloadInterruptException(Throwable cause) {
        super(ErrorCode.FILE_DOWNLOAD_INTERRUPT, cause);
    }
}
