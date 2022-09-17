package com.projectboated.backend.domain.task.task.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class CancelTaskAssignDeniedException extends BusinessException {

    public CancelTaskAssignDeniedException() {
        super(ErrorCode.PROJECT_ONLY_CAPTAIN_OR_CREW);
    }
}
