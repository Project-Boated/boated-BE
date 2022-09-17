package com.projectboated.backend.account.account.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountUsernameAlreadyExistsException extends BusinessException {
    public AccountUsernameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
