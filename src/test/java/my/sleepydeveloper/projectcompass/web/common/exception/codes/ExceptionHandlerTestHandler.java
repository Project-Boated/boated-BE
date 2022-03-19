package my.sleepydeveloper.projectcompass.web.common.exception.codes;

import my.sleepydeveloper.projectcompass.domain.exception.BaseBusinessException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotEmpty;

@Controller
public class ExceptionHandlerTestHandler {

    @GetMapping("/BaseBusinessException")
    public void baseBusinessException() {
        throw new BaseBusinessException(ErrorCode.FOR_TESTING);
    }

    @PostMapping("/HttpMessageNotReadableException")
    public void httpMessageNotReadableException(@RequestBody @Validated JsonForTest jsonForTest) {
    }

    @PostMapping("/MethodArgumentNotValidException")
    public void methodArgumentNotValidException(@RequestBody @Validated JsonForTest jsonForTest) {
    }

    @GetMapping("/Exception")
    public void exception() throws Exception {
        throw new Exception();
    }

    public static class JsonForTest {
        @Range(min = 0, max = 100)
        int value;

        @NotEmpty
        String value2;

        public JsonForTest() {
        }

        public JsonForTest(int value, String value2) {
            this.value = value;
            this.value2 = value2;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }


        public String getValue2() {
            return value2;
        }
    }

}
