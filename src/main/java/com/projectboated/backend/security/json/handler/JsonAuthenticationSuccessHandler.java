package com.projectboated.backend.security.json.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectboated.backend.account.account.entity.Account;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Account account = (Account) authentication.getPrincipal();
        objectMapper.writeValue(response.getWriter(), new LoginSuccessResponse(account));
    }

    @Getter
    static class LoginSuccessResponse {
        private Long id;

        public LoginSuccessResponse(Account account) {
            this.id = account.getId();
        }
    }
}
