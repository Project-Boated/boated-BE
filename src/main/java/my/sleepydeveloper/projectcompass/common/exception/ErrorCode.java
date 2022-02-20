package my.sleepydeveloper.projectcompass.common.exception;

public enum ErrorCode {

    // Example Exception, you can add more exception what you want
    COMMON_NOT_FOUND(404, "C001", "Not Found Exception"),
    COMMON_INVALID_VALUE(400, "C002", "Not Found Exception"),
    COMMON_VALIDATION_FAIL(400, "C003", "Fail Validation"),

    // Account Exception
    ACCOUNT_EXISTS_USERNAME(400, "U001", "이미 존재하는 아이디입니다."),
    ACCOUNT_EXISTS_NICKNAME(400, "U002", "이미 존재하는 닉네임입니다."),
    ACCOUNT_NOT_FOUND(400, "U003", "찾을 수 없는 account입니다"),
    ACCOUNT_WRONG_PASSWORD(400, "U004", "잘못된 password입니다"),
    ACCOUNT_DUPLICATE_NICKNAME(400, "U005", "중복된 nickname입니다"),
    ACCOUNT_UPDATE_ACCESS_DENIED(400, "U006", "Update불가, 권한없음."),

    // For Testing
    FOR_TESTING(507, "T001", "테스트용입니다.")

    ;

    private int status;
    private String statusCode;
    private String message;

    ErrorCode(int status, String statusCode, String message) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
