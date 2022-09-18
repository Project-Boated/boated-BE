package com.projectboated.backend.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectboated.backend.security.dto.UsernamePasswordDto;
import com.projectboated.backend.security.exception.IllegalRequestJsonLogin;
import com.projectboated.backend.security.exception.IllegalUsernamePassword;
import com.projectboated.backend.security.token.JsonAuthenticationToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/sign-in"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!isJsonAndPostRequest(request)) {
            throw new IllegalRequestJsonLogin("Authentication Request must have Json Header and POST method");
        }

        UsernamePasswordDto usernamePasswordDto = objectMapper.readValue(request.getReader(), UsernamePasswordDto.class);
        if (!StringUtils.hasText(usernamePasswordDto.getUsername()) || !StringUtils.hasText(usernamePasswordDto.getPassword())) {
            throw new IllegalUsernamePassword("Username or Password is empty");
        }

        JsonAuthenticationToken token = new JsonAuthenticationToken(usernamePasswordDto.getUsername(), usernamePasswordDto.getPassword());

        return getAuthenticationManager().authenticate(token);
    }

    private boolean isJsonAndPostRequest(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.CONTENT_TYPE).equals(MediaType.APPLICATION_JSON_VALUE)
                && request.getMethod().equals(HttpMethod.POST.name());
    }
}
