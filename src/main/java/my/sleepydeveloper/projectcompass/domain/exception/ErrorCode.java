package my.sleepydeveloper.projectcompass.domain.exception;

public enum ErrorCode {

    // Example Exception, you can add more exception what you want
    COMMON_NOT_FOUND(404, "C001", "Not Found Exception"),
    COMMON_INVALID_VALUE(400, "C002", "Not Found Exception"),
    COMMON_VALIDATION_FAIL(400, "C003", "Fail Validation"),
    COMMON_ACCESS_DENIED(400, "C004", "권한이 없습니다."),
    COMMON_IO_EXCEPTION(400, "C005", "IOException"),
    COMMON_NOT_IMAGE_FILE_EXCEPTION(400, "C006", "이미지 파일만 넣어주세요"),

    // Account Exception
    ACCOUNT_USERNAME_EXISTS(400, "U001", "이미 존재하는 아이디입니다."),
    ACCOUNT_NICKNAME_EXISTS(400, "U002", "이미 존재하는 닉네임입니다."),
    ACCOUNT_NOT_FOUND(400, "U003", "찾을 수 없는 account입니다"),
    ACCOUNT_PASSWORD_WRONG(400, "U004", "잘못된 password입니다"),
    ACCOUNT_NICKNAME_REQUIRED(403, "U007", "닉네임이 필요합니다."),
    ACCOUNT_PROFILE_IMAGE_FILE_NOT_EXIST(400, "U008", "Account의 프로필 이미지 파일이 없습니다."),
    ACCOUNT_PROFILE_IMAGE_FILE_NOT_HOST(400, "U009", "Account의 프로필 이미지가 host가 아닙니다"),
    ACCOUNT_PROFILE_IMAGE_NOT_UPLOAD_FILE(400, "U010", "Account의 프로필 이미지가 Upload되지 않았습니다."),

    // Project
    PROJECT_NOT_FOUND(400, "P002", "Project를 찾을 수 없습니다"),
    PROJECT_CAPTAIN_UPDATE_ACCESS_DENIED(400, "P003", "Captain을 Update할 수 없습니다. 권한 부족"),
    PROJECT_NAME_EXISTS_IN_ACCOUNT(400, "P001", "Account에 같은 이름의 프로젝트가 존재합니다."),

    // Kakao
    KAKAO_SERVER_EXCEPTION(400, "K001", "카카오 서버 오류입니다."),

    // File
    FILE_UPLOAD_INTERRUPT(400, "F001", "파일 업로드 인터럽트 오류"),

    // Invite
    INVITATION_DO_NOT_INVITE_CAPTAIN(400, "IV001", "Captain을 invite할 수 없습니다"),
    INVITATION_ACCOUNT_EXISTS(400, "IV002", "이미 초대를 보낸 Account입니다."),
    INVITATION_ACCOUNT_EXISTS_IN_PROJECT(400, "IV003", "이미 Crew인 Account입니다."),
    INVITATION_NOT_FOUND(400, "IV004", "찾을 수 없는 초대입니다."),

    // For Testing
    FOR_TESTING(507, "T001", "테스트용입니다.");

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
