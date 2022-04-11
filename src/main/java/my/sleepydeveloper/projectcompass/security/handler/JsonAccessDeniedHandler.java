package my.sleepydeveloper.projectcompass.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.sleepydeveloper.projectcompass.domain.exception.BusinessException;
import my.sleepydeveloper.projectcompass.web.exception.dto.BasicErrorResponse;
import my.sleepydeveloper.projectcompass.web.exception.dto.ExceptionMessageResponse;

public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        
        if(accessDeniedException instanceof BusinessException) {
            BasicErrorResponse basicErrorResponse = new BasicErrorResponse(((BusinessException)accessDeniedException).getErrorCode());
            objectMapper.writeValue(response.getWriter(), basicErrorResponse);
            return;
        }

        objectMapper.writeValue(response.getWriter(), new ExceptionMessageResponse("Access Denied"));
    }
}
