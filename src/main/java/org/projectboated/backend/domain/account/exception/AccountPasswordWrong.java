package org.projectboated.backend.domain.account.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class AccountPasswordWrong extends BusinessException {
    public AccountPasswordWrong(ErrorCode errorCode) {
        super(errorCode);
    }
}
