package com.projectboated.backend.web.account.account.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class NotImageFileException extends BusinessException {
    public NotImageFileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
