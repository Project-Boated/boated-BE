package org.projectboated.backend.domain.account.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class AccountProfileImageFileNotExist extends BusinessException {
    public AccountProfileImageFileNotExist(ErrorCode errorCode) {
        super(errorCode);
    }

    public AccountProfileImageFileNotExist(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
