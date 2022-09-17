package com.projectboated.backend.domain.task.tasklike.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class CancelTaskLikeAccessDeniedException extends BusinessException {
    public CancelTaskLikeAccessDeniedException() {
        super(ErrorCode.PROJECT_ONLY_CAPTAIN_OR_CREW);
    }
}
