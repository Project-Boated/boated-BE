package my.sleepydeveloper.projectcompass.domain.exception;

import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public interface BusinessException {
	ErrorCode getErrorCode();
}
