package com.projectboated.backend.account.account.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class AccountPasswordWrong extends BusinessException {
    public AccountPasswordWrong() {
        super(ErrorCode.ACCOUNT_PASSWORD_WRONG);
    }
}
