package com.projectboated.backend.domain.task.task.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountTaskNotFoundException extends BusinessException {
    public AccountTaskNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
