package com.projectboated.backend.aws.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class AccountProfileImageNotUploadFile extends BusinessException {
    public AccountProfileImageNotUploadFile(ErrorCode errorCode) {
        super(errorCode);
    }
}
