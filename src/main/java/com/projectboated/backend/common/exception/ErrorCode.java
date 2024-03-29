package com.projectboated.backend.common.exception;

public enum ErrorCode {

    // Example Exception, you can add more exception what you want
    COMMON_NOT_FOUND(404, "C001", "Not Found Exception"),
    COMMON_INVALID_VALUE(400, "C002", "Not Found Exception"),
    COMMON_VALIDATION_FAIL(400, "C003", "Fail Validation"),
    COMMON_ACCESS_DENIED(400, "C004", "권한이 없습니다."),
    COMMON_IO_EXCEPTION(400, "C005", "IOException"),
    COMMON_NOT_IMAGE_FILE_EXCEPTION(400, "C006", "이미지 파일만 넣어주세요"),
    COMMON_MULTIPART_FILE_IS_EMPTY(400, "C007", "multipart file이 비어있습니다."),

    // Account Exception
    ACCOUNT_USERNAME_EXISTS(400, "U001", "이미 존재하는 아이디입니다."),
    ACCOUNT_NICKNAME_EXISTS(400, "U002", "이미 존재하는 닉네임입니다."),
    ACCOUNT_NOT_FOUND(400, "U003", "찾을 수 없는 account입니다"),
    ACCOUNT_PASSWORD_WRONG(400, "U004", "잘못된 password입니다"),
    ACCOUNT_NICKNAME_REQUIRED(403, "U007", "닉네임이 필요합니다."),
    ACCOUNT_PROFILE_IMAGE_FILE_NOT_EXIST(400, "U008", "Account의 프로필 이미지 파일이 없습니다."),
    ACCOUNT_PROFILE_IMAGE_FILE_NOT_HOST(400, "U009", "Account의 프로필 이미지가 host가 아닙니다"),
    ACCOUNT_PROFILE_IMAGE_NOT_UPLOAD_FILE(400, "U010", "Account의 프로필 이미지가 Upload되지 않았습니다."),
    ACCOUNT_PROFILE_IMAGE_NOT_SUPPORT_TYPE(400, "U011", "Accont의 프로필 이미지가 지원하지 않는 Type입니다."),
    ACCOUNT_NOT_FOUND_BY_USERNAME(400, "U012", "주어진 username으로 account를 찾을 수 없습니다"),

    // UploodFile ProfileImage
    PROFILE_IMAGE_NEEDS_HOST_URL(400, "UFPI001", "host url이 필요합니다. 헤더에 추가해주세요"),

    // Project
    PROJECT_NAME_EXISTS_IN_ACCOUNT(400, "P001", "Account에 같은 이름의 프로젝트가 존재합니다."),
    PROJECT_NOT_FOUND(400, "P002", "Project를 찾을 수 없습니다"),
    PROJECT_CAPTAIN_UPDATE_DENIED_NOT_CREW(400, "P004", "Captain을 Update할 수 없습니다. username이 crew가 아닙니다"),
    PROJECT_DOESNT_HAVE_KANBAN_LANE(400, "P005", "Project가 해당 kanbanlane을 가지고 있지 않습니다."),
    PROJECT_ONLY_CAPTAIN_OR_CREW(400, "P007", "Captain이나 Crew만 가능합니다. 권한부족."),
    PROJECT_ONLY_CAPTAIN(400, "P007", "Captain만 가능합니다. 권한부족."),
    PROJECT_TOTAL_FILE_SIZE_IS_MINUS(400, "P008", "프로젝트의 총 size가 마이너스입니다."),

    // Crew
    ACCOUNT_PROJECT_NOT_FOUND(400, "CR001", "crew를 찾을 수 없습니다."),

    // Task
    TASK_NOT_FOUND(400, "T001", "task를 찾을 수 없습니다."),
    TASK_ALREADY_ASSIGNED(400, "T003", "이미 해당 task에 배정받은 Account입니다."),
    ACCOUNT_TASK_NOT_FOUND(400, "T004", "배정되지 않은 account입니다."),
    TASK_ORIGINAL_INDEX_OUT_OF_BOUNDS(400, "T006", "original index가 lane index범위를 벗어났습니다."),
    TASK_CHANGE_INDEX_OUT_OF_BOUNDS(400, "T007", "change index가 lane index범위를 벗어났습니다."),

    // Task Like
    TASK_LIKE_ALREADY_EXISTS(400, "TL001", "이미 찜한 task입니다."),
    TASK_LIKE_NOT_FOUND(400, "TL002", "찾을 수 없는 찜입니다."),
    TASK_LIKE_ORIGINAL_INDEX_OUT_OF_BOUNDS(400, "TL003", "original index가 tasklike index범위를 벗어났습니다."),
    TASK_LIKE_CHNAGE_INDEX_OUT_OF_BOUNDS(400, "TL004", "change index가 tasklike index범위를 벗어났습니다."),

    // Task File
    TASK_FILE_NOT_FOUND(400, "TF001", "task file을 찾을 수 없습니다."),

    // Kakao
    KAKAO_SERVER_EXCEPTION(400, "K001", "카카오 서버 오류입니다."),

    // File
    FILE_UPLOAD_INTERRUPT(400, "F001", "파일 업로드 인터럽트 오류"),
    FILE_DOWNLOAD_INTERRUPT(400, "F002", "파일 다운로드 인터럽트 오류"),

    // Invite
    INVITATION_DO_NOT_INVITE_CAPTAIN(400, "IV001", "Captain을 invite할 수 없습니다"),
    INVITATION_EXISTS(400, "IV002", "이미 초대를 보낸 Account입니다."),
    INVITATION_ACCOUNT_EXISTS_IN_PROJECT(400, "IV003", "이미 Crew인 Account입니다."),
    INVITATION_NOT_FOUND(400, "IV004", "찾을 수 없는 초대입니다."),
    INVITATION_DO_NOT_INVITE_CREW(400, "IV005", "이미 crew인 account를 초대할 수 없습니다."),

    // Kanban
    KANBAN_NOT_FOUND(400, "KN001", "kanban을 찾을 수 없습니다."),

    // Kanban Lane
    KANBAN_LANE_EXISTS_UPPER_5(400, "KL001", "칸반 lane의 개수가 이미 5개입니다"),
    KANBAN_LANE_EXISTS_LOWER_1(400, "KL007", "칸반 lane의 개수가 1개입니다."),
    KANBAN_LANE_NOT_FOUND(400, "KL002", "칸반 lane을 찾을 수 없습니다"),
    KANBAN_LANE_ORIGINAL_INDEX_OUT_OF_BOUNDS(400, "KL004", "original index가 lane index범위를 벗어났습니다."),
    KANBAN_LANE_CHANGE_INDEX_OUT_OF_BOUNDS(400, "KL005", "change index가 lane index범위를 벗어났습니다."),
    KANBAN_LANE_NOT_IN_PROJECT(400, "KL006", "해당 Kanban lane이 project에 존재하지 않습니다"),
    KANBAN_LANE_EXISTS_TASK(400, "KL007", "kanban lane에 task가 존재합니다"),

    // Upload File
    UPLOAD_FILE_NOT_FOUND_EXT(400, "UF001", "upload file에 ext를 찾을 수 없습니다."),

    // Project Video
    PROJECT_VIDEO_IS_PRESENT(400, "PV001", "project video가 이미 존재합니다."),
    PROJECT_VIDEO_NOT_FOUND(400, "PV002", "project video를 찾을 수 없습니다."),

    // Project Chatting Room
    PROJECT_CHATTING_ROOM_NOT_FOUND(400, "PR001", "project chatting room을 찾을 수 없습니다"),

    // AOP
    AOP_PROJECT_ID_NOT_FOUND(400, "AO001", "project id를 찾을 수 없습니다. 이 에러는 서버에러입니다. 카톡주세요."),

    // Login
    LOGIN_IS_NOT_JSON_REQUEST(400, "LO001", "json으로 요청해주십시오."),
    LOGIN_USERNAME_PASSWORD_REQUIRED(400, "LO002", "username이나 password를 채워서 보내주세요."),
    LOGIN_IS_NOT_KAKAO_REQUEST(400, "LO003", "카카오 로그인이 아닙니다."),
    LOGIN_KAKAO_AUTHENTICATION_EXCEPTION(400, "L004", "카카오 내부 에러"),
    LOGIN_PASSWORD_NOT_MATCHES(400, "L005", "password가 일치하지 않습니다"),

    // Json
    JSON_PARSING_EXCEPTION(400, "JS001", "json parsing error"),

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
