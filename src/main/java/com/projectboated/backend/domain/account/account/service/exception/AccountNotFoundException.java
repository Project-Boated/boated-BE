package com.projectboated.backend.domain.account.account.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
