package com.projectboated.backend.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectboated.backend.domain.common.exception.ErrorCodeException;
import com.projectboated.backend.web.exception.dto.BasicErrorResponse;
import com.projectboated.backend.web.exception.dto.ExceptionMessageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");

        if (accessDeniedException instanceof ErrorCodeException) {
            BasicErrorResponse basicErrorResponse = new BasicErrorResponse(((ErrorCodeException) accessDeniedException).getErrorCode());
            objectMapper.writeValue(response.getWriter(), basicErrorResponse);
            return;
        }

        objectMapper.writeValue(response.getWriter(), new ExceptionMessageResponse("Access Denied"));
    }
}
