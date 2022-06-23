package org.projectboated.backend.domain.account.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class AccountProfileImageNotSupportTypeException extends BusinessException {

    public AccountProfileImageNotSupportTypeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
