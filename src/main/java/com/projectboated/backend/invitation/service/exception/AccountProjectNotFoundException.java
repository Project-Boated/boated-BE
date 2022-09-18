package com.projectboated.backend.invitation.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class AccountProjectNotFoundException extends BusinessException {
    public AccountProjectNotFoundException() {
        super(ErrorCode.ACCOUNT_PROJECT_NOT_FOUND);
    }
}
