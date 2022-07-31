package com.projectboated.backend.domain.task.taskfile.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class UploadTaskFileAccessDeniedException extends BusinessException {
    public UploadTaskFileAccessDeniedException() {
        super(ErrorCode.PROJECT_ONLY_CAPTAIN_OR_CREW);
    }
}
