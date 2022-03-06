package my.sleepydeveloper.projectcompass.web.exception.dto;

import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;

public class BasicErrorResponse {

    private int status;
    private String statusCode;
    private String message;


    public BasicErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.statusCode = errorCode.getStatusCode();
        this.message = errorCode.getMessage();
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
