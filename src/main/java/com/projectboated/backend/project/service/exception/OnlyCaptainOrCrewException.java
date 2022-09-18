package com.projectboated.backend.project.service.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;

public class OnlyCaptainOrCrewException extends BusinessException {
    public OnlyCaptainOrCrewException() {
        super(ErrorCode.PROJECT_ONLY_CAPTAIN_OR_CREW);
    }
}
