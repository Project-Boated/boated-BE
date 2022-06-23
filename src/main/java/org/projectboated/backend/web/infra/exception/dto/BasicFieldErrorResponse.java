package org.projectboated.backend.web.infra.exception.dto;

import org.projectboated.backend.domain.exception.ErrorCode;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Locale;

public class BasicFieldErrorResponse extends BasicErrorResponse {

    private List<BasicFieldError> fieldErrors;

    public BasicFieldErrorResponse(BindingResult bindingResult, MessageSource messageSource, Locale locale) {
        super(ErrorCode.COMMON_VALIDATION_FAIL);
        this.fieldErrors = BasicFieldError.of(bindingResult, messageSource, locale);
    }

    public List<BasicFieldError> getFieldErrors() {
        return fieldErrors;
    }
}
