package com.projectboated.backend.domain.invitation.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountProjectNotFoundException extends BusinessException {
    public AccountProjectNotFoundException() {
        super(ErrorCode.ACCOUNT_PROJECT_NOT_FOUND);
    }
}