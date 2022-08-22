package com.projectboated.backend.web.common.exception.dto;

import com.projectboated.backend.common.basetest.BaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BasicFieldError : error 단위테스트")
class BasicFieldErrorTest extends BaseTest {

    @Test
    void 생성자_모든필드주어짐_return_BasicFieldError() {
        // Given
        String field = "field1";
        String value = "bad";
        String reason = "field1 is bad";

        // When
        BasicFieldError basicFieldError = new BasicFieldError(field, value, reason);

        // Then
        assertThat(basicFieldError.getField()).isEqualTo(field);
        assertThat(basicFieldError.getValue()).isEqualTo(value);
        assertThat(basicFieldError.getReason()).isEqualTo(reason);
    }

//    @Test
//    void of_1개error주어짐_return_BasicFieldErrors() {
//        // Given
//        Map<String, String> map = new HashMap<>();
//        String objectName = "objectName";
//        MapBindingResult bindingResult = new MapBindingResult(map, objectName);
//        MessageSource messageSource = new MockMessageSource();
//
//        // When
//
//        // Then
//    }

}