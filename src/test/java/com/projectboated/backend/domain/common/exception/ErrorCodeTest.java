package com.projectboated.backend.domain.common.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ErrorCode : enum 테스트")
class ErrorCodeTest {

    @Test
    void getStatus_testdata주어짐_return_status() {
        // Given
        ErrorCode errorCode = ErrorCode.FOR_TESTING;

        // When
        int status = errorCode.getStatus();

        // Then
        assertThat(status).isEqualTo(507);
    }

    @Test
    void getStatusCode_testdata주어짐_return_statusCode() {
        // Given
        ErrorCode errorCode = ErrorCode.FOR_TESTING;

        // When
        String statusCode = errorCode.getStatusCode();

        // Then
        assertThat(statusCode).isEqualTo("T001");
    }

    @Test
    void getMessage_testdata주어짐_return_message() {
        // Given
        ErrorCode errorCode = ErrorCode.FOR_TESTING;

        // When
        String message = errorCode.getMessage();

        // Then
        assertThat(message).isEqualTo("테스트용입니다.");
    }

}