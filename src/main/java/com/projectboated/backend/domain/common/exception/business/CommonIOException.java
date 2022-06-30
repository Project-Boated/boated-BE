package com.projectboated.backend.domain.common.exception.business;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class CommonIOException extends BusinessException {
    public CommonIOException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
