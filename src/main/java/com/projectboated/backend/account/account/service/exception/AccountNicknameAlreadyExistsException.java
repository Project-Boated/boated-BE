package com.projectboated.backend.account.account.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class AccountNicknameAlreadyExistsException extends BusinessException {
    public AccountNicknameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
