package com.example.projectcompass.web.common.exception.dto;

import com.example.projectcompass.domain.common.exception.ErrorCode;
import com.example.projectcompass.web.common.exception.BasicFieldError;
import com.example.projectcompass.web.common.exception.dto.BasicErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class BasicFieldErrorResponse extends BasicErrorResponse {

    private List<BasicFieldError> fieldErrors;

    public BasicFieldErrorResponse(ErrorCode errorCode) {
        super(errorCode);
        this.fieldErrors = new ArrayList<>();
    }

    public BasicFieldErrorResponse(ErrorCode errorCode, BindingResult bindingResult, MessageSource messageSource) {
        super(errorCode);
        this.fieldErrors = BasicFieldError.of(bindingResult, messageSource);
    }

    public List<BasicFieldError> getFieldErrors() {
        return fieldErrors;
    }
}
