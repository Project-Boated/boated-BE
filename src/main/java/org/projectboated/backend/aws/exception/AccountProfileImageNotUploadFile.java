package org.projectboated.backend.aws.exception;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class AccountProfileImageNotUploadFile extends BusinessException {
    public AccountProfileImageNotUploadFile(ErrorCode errorCode) {
        super(errorCode);
    }
}
