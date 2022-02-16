package com.example.projectcompass.web.common.exception.dto;

import com.example.projectcompass.domain.common.exception.ErrorCode;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

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
