package com.projectboated.backend.domain.account.account.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountProfileImageNotSupportTypeException extends BusinessException {

    public AccountProfileImageNotSupportTypeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
