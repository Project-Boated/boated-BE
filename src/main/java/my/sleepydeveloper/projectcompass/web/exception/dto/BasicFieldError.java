package my.sleepydeveloper.projectcompass.web.exception.dto;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
public class BasicFieldError {

    private String field;
    private String value;
    private String reason;

    public BasicFieldError(String field, String value, String reason) {
        this.field = field;
        this.value = value;
        this.reason = reason;
    }

    public static List<BasicFieldError> of(BindingResult bindingResult, MessageSource messageSource, Locale locale) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
                .map(error -> new BasicFieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            getMessage(error, messageSource, locale)))
                .collect(Collectors.toList());
    }

    private static String getMessage(FieldError error, MessageSource messageSource, Locale locale) {
        String result = error.getDefaultMessage();
        for (String code : error.getCodes()) {
            String messageNotFound = "NotFound";
            String message = messageSource.getMessage(code, null, messageNotFound, locale);
            if(!message.equals(messageNotFound)) {
                result = message;
            }
        }
        return result;
    }

}
