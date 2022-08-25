package com.projectboated.backend.domain.projectvideo.service.exception;

import com.projectboated.backend.domain.common.exception.BusinessException;
import com.projectboated.backend.domain.common.exception.ErrorCode;

public class ProjectVideoIsPresentException extends BusinessException {
    public ProjectVideoIsPresentException() {
        super(ErrorCode.PROJECT_VIDEO_IS_PRESENT);
    }
}
