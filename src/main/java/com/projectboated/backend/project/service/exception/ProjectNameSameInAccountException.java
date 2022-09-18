package com.projectboated.backend.project.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class ProjectNameSameInAccountException extends BusinessException {
    public ProjectNameSameInAccountException() {
        super(ErrorCode.PROJECT_NAME_EXISTS_IN_ACCOUNT);
    }
}
