package com.projectboated.backend.task.task.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class AccountTaskNotFoundException extends BusinessException {
    public AccountTaskNotFoundException() {
        super(ErrorCode.ACCOUNT_TASK_NOT_FOUND);
    }
}
