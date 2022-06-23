package org.projectboated.backend.domain.account.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class AccountNicknameAlreadyExistsException extends BusinessException {
    public AccountNicknameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
