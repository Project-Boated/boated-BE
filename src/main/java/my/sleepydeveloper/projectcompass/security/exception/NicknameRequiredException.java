package my.sleepydeveloper.projectcompass.security.exception;

import org.springframework.security.access.AccessDeniedException;

import my.sleepydeveloper.projectcompass.domain.exception.ErrorCodeException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

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
