package my.sleepydeveloper.projectcompass.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.security.dto.IdDto;

public class KakaoAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Account account = (Account) authentication.getPrincipal();
        IdDto idDto = new IdDto(account.getId());
    
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    
        objectMapper.writeValue(response.getWriter(), idDto);
    }
}
