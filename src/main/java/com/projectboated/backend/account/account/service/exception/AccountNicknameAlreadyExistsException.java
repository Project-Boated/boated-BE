package com.projectboated.backend.account.account.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountNicknameAlreadyExistsException extends BusinessException {
    public AccountNicknameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
