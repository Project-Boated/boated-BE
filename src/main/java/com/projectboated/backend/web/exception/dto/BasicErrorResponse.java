package com.projectboated.backend.web.exception.dto;

import com.projectboated.backend.domain.common.exception.ErrorCode;

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
