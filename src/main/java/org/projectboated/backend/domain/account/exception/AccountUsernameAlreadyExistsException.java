package org.projectboated.backend.domain.account.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class AccountUsernameAlreadyExistsException extends BusinessException {
    public AccountUsernameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
