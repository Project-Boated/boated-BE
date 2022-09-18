package com.projectboated.backend.security.json.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectboated.backend.security.json.filter.dto.UsernamePasswordDto;
import com.projectboated.backend.security.json.filter.exception.IsNotJsonLoginRequestException;
import com.projectboated.backend.security.json.filter.exception.UsernameAndPasswordRequiredException;
import com.projectboated.backend.security.json.token.JsonAuthenticationToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
        validateRequest(request);

        UsernamePasswordDto usernamePasswordDto = objectMapper.readValue(request.getReader(), UsernamePasswordDto.class);
        if (!hasUsernameAndPassword(usernamePasswordDto)) {
            throw new UsernameAndPasswordRequiredException();
        }

        JsonAuthenticationToken token = new JsonAuthenticationToken(usernamePasswordDto.getUsername(), usernamePasswordDto.getPassword());
        return getAuthenticationManager().authenticate(token);
    }

    private void validateRequest(HttpServletRequest request) {
        if (!isJsonRequest(request)) {
            throw new IsNotJsonLoginRequestException();
        }
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.CONTENT_TYPE).equals(MediaType.APPLICATION_JSON_VALUE);
    }

    private static boolean hasUsernameAndPassword(UsernamePasswordDto usernamePasswordDto) {
        return !usernamePasswordDto.getUsername().isBlank() && !usernamePasswordDto.getPassword().isBlank();
    }
}
