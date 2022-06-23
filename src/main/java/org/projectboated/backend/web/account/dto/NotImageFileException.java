package org.projectboated.backend.web.account.dto;

import org.projectboated.backend.domain.exception.BusinessException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class NotImageFileException extends BusinessException {
    public NotImageFileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
