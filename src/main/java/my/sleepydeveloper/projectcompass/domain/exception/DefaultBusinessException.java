package my.sleepydeveloper.projectcompass.domain.exception;

public class DefaultBusinessException extends RuntimeException implements BusinessException{

    private ErrorCode errorCode;

    public DefaultBusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
