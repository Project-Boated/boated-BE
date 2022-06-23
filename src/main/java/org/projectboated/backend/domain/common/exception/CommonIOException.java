package org.projectboated.backend.domain.common.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class CommonIOException extends BusinessException {
    public CommonIOException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
