package com.projectboated.backend.common.exception;

import com.projectboated.backend.common.exception.BusinessException;
import com.projectboated.backend.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BusinessException : common 테스트")
class BusinessExceptionTest {

    @Test
    void 생성자_errorCode만주어짐_생성성공() {
        // Given
        // When
        // Then
        BusinessException businessException = new BusinessException(ErrorCode.FOR_TESTING);
    }

    @Test
    void 생성자_errorCode와cause주어짐_생성성공() {
        // Given
        // When
        // Then
        BusinessException businessException = new BusinessException(ErrorCode.FOR_TESTING, new RuntimeException());
    }

    @Test
    void getErrorCode_testdata주어짐_return_errorcode() {
        // Given
        BusinessException businessException = new BusinessException(ErrorCode.FOR_TESTING, new RuntimeException());

        // When
        ErrorCode errorCode = businessException.getErrorCode();

        // Then
        assertThat(errorCode).isEqualTo(ErrorCode.FOR_TESTING);
    }

}