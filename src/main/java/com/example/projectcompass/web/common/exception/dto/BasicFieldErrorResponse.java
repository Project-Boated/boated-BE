package com.example.projectcompass.web.common.exception.dto;

import com.example.projectcompass.domain.common.exception.ErrorCode;
import com.example.projectcompass.web.common.exception.BasicFieldError;
import com.example.projectcompass.web.common.exception.dto.BasicErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BasicFieldErrorResponse extends BasicErrorResponse {

    private List<BasicFieldError> fieldErrors;

    public BasicFieldErrorResponse(ErrorCode errorCode) {
        super(errorCode);
        this.fieldErrors = new ArrayList<>();
    }

    public BasicFieldErrorResponse(BindingResult bindingResult, MessageSource messageSource, Locale locale) {
        super(ErrorCode.COMMON_VALIDATION_FAIL);
        this.fieldErrors = BasicFieldError.of(bindingResult, messageSource, locale);
    }

    public List<BasicFieldError> getFieldErrors() {
        return fieldErrors;
    }
}
