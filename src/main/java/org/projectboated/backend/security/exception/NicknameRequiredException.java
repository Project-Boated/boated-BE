package org.projectboated.backend.security.exception;

import org.springframework.security.access.AccessDeniedException;

import org.projectboated.backend.domain.exception.ErrorCodeException;
import org.projectboated.backend.domain.exception.ErrorCode;

public class NicknameRequiredException extends AccessDeniedException implements ErrorCodeException {
	
	private final ErrorCode errorCode;
	
	public NicknameRequiredException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
	
	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
