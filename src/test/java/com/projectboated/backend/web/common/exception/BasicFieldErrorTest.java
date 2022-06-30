package com.projectboated.backend.web.common.exception;

import com.projectboated.backend.web.infra.exception.dto.BasicFieldError;
import com.projectboated.backend.common.basetest.BaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@SpringBootTest
@ActiveProfiles("deploy")
class BasicFieldErrorTest extends BaseTest {

    @Autowired
    MessageSource messageSource;

    @Test
    void basicFieldErrorTest_message존재하는경우_message반환() {
        // Given
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "test");
        bindingResult.rejectValue("test", "Test", null);

        // When
        List<BasicFieldError> basicFieldErrors = BasicFieldError.of(bindingResult, messageSource, Locale.KOREA);

        // Then
        Assertions.assertThat(basicFieldErrors.get(0).getReason()).isEqualTo("Testing");
    }

    @Test
    void basicFieldErrorTest_message가존재하지않는경우_defaultMessage반환() {
        // Given
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "test");
        bindingResult.rejectValue("test", "failure", "default");

        // When
        List<BasicFieldError> basicFieldErrors = BasicFieldError.of(bindingResult, messageSource, Locale.KOREA);

        // Then
        Assertions.assertThat(basicFieldErrors.get(0).getReason()).isEqualTo("default");
    }
}