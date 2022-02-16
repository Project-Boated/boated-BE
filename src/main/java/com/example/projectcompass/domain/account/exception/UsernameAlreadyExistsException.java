package com.example.projectcompass.domain.account.exception;

import com.example.projectcompass.domain.common.exception.BaseBusinessException;
import com.example.projectcompass.domain.common.exception.ErrorCode;

public class UsernameAlreadyExistsException extends BaseBusinessException {
    public UsernameAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
