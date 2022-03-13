package my.sleepydeveloper.projectcompass.web.common.exception;

import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.web.exception.GlobalExceptionHandler;
import my.sleepydeveloper.projectcompass.web.common.exception.codes.ExceptionHandlerTestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ExceptionHandlerTestHandler.class, GlobalExceptionHandler.class},
        excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSecurityConfigurer.class) },
        excludeAutoConfiguration = { SecurityAutoConfiguration.class})
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
class GlobalExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    ErrorCode errorCode = ErrorCode.FOR_TESTING;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("BaseBusinessException 테스트")
    void BaseBusinessException() throws Exception {

        mockMvc.perform(get("/BaseBusinessException"))
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("status").value(errorCode.getStatus()))
                .andExpect(jsonPath("statusCode").value(errorCode.getStatusCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    @DisplayName("HttpMessageNotReadableException 테스트")
    void HttpMessageNotReadableException() throws Exception {

        mockMvc.perform(post("/HttpMessageNotReadableException")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content("fail"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists());
    }

    @Test
    @DisplayName("MethodArgumentNotValidException 테스트")
    void MethodArgumentNotValidException() throws Exception {

        mockMvc.perform(post("/MethodArgumentNotValidException")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new ExceptionHandlerTestHandler.JsonForTest(123, ""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").exists())
                .andExpect(jsonPath("statusCode").exists())
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("fieldErrors[0].field").exists())
                .andExpect(jsonPath("fieldErrors[0].value").exists())
                .andExpect(jsonPath("fieldErrors[0].reason").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("Exception 테스트")
    void Exception() throws Exception {

        mockMvc.perform(get("/Exception")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").exists());
    }

}