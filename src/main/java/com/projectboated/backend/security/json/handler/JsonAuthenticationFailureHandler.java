package com.projectboated.backend.security.json.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectboated.backend.common.exception.ErrorCodeException;
import com.projectboated.backend.web.exception.dto.BasicErrorResponse;
import com.projectboated.backend.web.exception.dto.ExceptionMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (exception instanceof ErrorCodeException errorCodeException) {
            BasicErrorResponse basicErrorResponse = new BasicErrorResponse(errorCodeException.getErrorCode());
            objectMapper.writeValue(response.getWriter(), basicErrorResponse);
            return;
        }

        objectMapper.writeValue(response.getWriter(), new ExceptionMessageResponse(exception.getMessage()));
    }
}
