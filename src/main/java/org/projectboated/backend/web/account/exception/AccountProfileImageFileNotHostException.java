package org.projectboated.backend.web.account.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class AccountProfileImageFileNotHostException extends BusinessException {
    public AccountProfileImageFileNotHostException(ErrorCode errorCode) {
        super(errorCode);
    }
}
