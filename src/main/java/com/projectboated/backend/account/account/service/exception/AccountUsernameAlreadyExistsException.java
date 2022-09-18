package com.projectboated.backend.account.account.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class AccountUsernameAlreadyExistsException extends BusinessException {
    public AccountUsernameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
