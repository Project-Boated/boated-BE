package my.sleepydeveloper.projectcompass.web.common.exception;

import my.sleepydeveloper.projectcompass.web.exception.dto.BasicFieldError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BasicFieldErrorTest {

    @Mock
    BindingResult bindingResult;

    @Mock
    MessageSource messageSource;

    @Test
    @DisplayName("messagesource에서 찾을 수 있을 때")
    void basicFieldErrorTest_find_in_messagesource() {
        // Given
        ArrayList<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("object", "field", null, false, new String[]{"NotEmpty"}, null, null));
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        String messageSourceMessage = "비어있을 수 없습니다.";
        Mockito.when(messageSource.getMessage("NotEmpty", null, "NotFound", Locale.KOREA))
                .thenReturn(messageSourceMessage);

        // When
        List<BasicFieldError> basicFieldErrors = BasicFieldError.of(bindingResult, messageSource, Locale.KOREA);

        // Then
        Assertions.assertThat(basicFieldErrors.get(0).getReason()).isEqualTo(messageSourceMessage);
    }

    @Test
    @DisplayName("messagesource에서 찾을 수 없을 때")
    void basicFieldErrorTest_not_found_in_messagesource() {
        // Given
        String defaultMessage = "defaultMessage";
        ArrayList<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("object", "field", null, false, new String[]{"NotEmpty"}, null, defaultMessage));

        Mockito.when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        Mockito.when(messageSource.getMessage("NotEmpty", null, "NotFound", Locale.KOREA))
                .thenReturn("NotFound");

        // When
        List<BasicFieldError> basicFieldErrors = BasicFieldError.of(bindingResult, messageSource, Locale.KOREA);

        // Then
        Assertions.assertThat(basicFieldErrors.get(0).getReason()).isEqualTo(defaultMessage);
    }
}