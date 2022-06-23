package org.projectboated.backend.domain.account.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
