package com.projectboated.backend.security.kakao.filter;

import com.projectboated.backend.security.kakao.filter.exception.IsNotKakaoLoginRequestException;
import com.projectboated.backend.security.kakao.filter.exception.KakaoAuthenticationException;
import com.projectboated.backend.security.kakao.token.KakaoAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class KakaoAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public KakaoAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/sign-in/kakao"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        validateRequest(request);

        String code = request.getParameter("code");

        KakaoAuthenticationToken token = new KakaoAuthenticationToken(code, null);
        return getAuthenticationManager().authenticate(token);
    }

    private void validateRequest(HttpServletRequest request) {
        if (!isKakaoLoginRequest(request)) {
            throw new IsNotKakaoLoginRequestException();
        }
        if (hasErrors(request)) {
            throw new KakaoAuthenticationException();
        }
    }

    private boolean isKakaoLoginRequest(HttpServletRequest request) {
        return request.getParameter("code") != null;
    }

    private boolean hasErrors(HttpServletRequest request) {
        return request.getParameter("error") != null;
    }
}
