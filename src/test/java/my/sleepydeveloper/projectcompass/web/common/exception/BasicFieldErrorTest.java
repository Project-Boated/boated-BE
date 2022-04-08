package my.sleepydeveloper.projectcompass.web.common.exception;

import my.sleepydeveloper.projectcompass.common.basetest.UnitTest;
import my.sleepydeveloper.projectcompass.web.exception.dto.BasicFieldError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@SpringBootTest
class BasicFieldErrorTest extends UnitTest {

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