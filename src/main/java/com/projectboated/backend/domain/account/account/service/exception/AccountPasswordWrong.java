package com.projectboated.backend.domain.account.account.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountPasswordWrong extends BusinessException {
    public AccountPasswordWrong(ErrorCode errorCode) {
        super(errorCode);
    }
}
