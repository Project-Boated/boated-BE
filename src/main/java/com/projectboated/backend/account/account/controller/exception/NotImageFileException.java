package com.projectboated.backend.account.account.controller.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class NotImageFileException extends BusinessException {
    public NotImageFileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
