package com.projectboated.backend.account.account.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException() {
        super(ErrorCode.ACCOUNT_NOT_FOUND);
    }

    public AccountNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
