package com.projectboated.backend.domain.account.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountNicknameAlreadyExistsException extends BusinessException {
    public AccountNicknameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
