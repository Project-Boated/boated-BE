package org.projectboated.backend.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.projectboated.backend.security.exception.IsNotKakaoLoginRequest;
import org.projectboated.backend.security.exception.KakaoAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.projectboated.backend.security.token.KakaoAuthenticationToken;

public class KakaoAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    
    
    Logger log = LoggerFactory.getLogger(KakaoAuthenticationFilter.class);
    
    public KakaoAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/sign-in/kakao"));
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!isKakaoLoginRequest(request)) {
            throw new IsNotKakaoLoginRequest("is not kakao login request.");
        }
        
        if (hasErrors(request)) {
            throw new KakaoAuthenticationException("Kakao Login Exception");
        }
        
        String code = request.getParameter("code");
        KakaoAuthenticationToken token = new KakaoAuthenticationToken(code, null);
        
        return getAuthenticationManager().authenticate(token);
    }
    
    private boolean hasErrors(HttpServletRequest request) {
        return request.getParameter("error") != null;
    }
    
    private boolean isKakaoLoginRequest(HttpServletRequest request) {
        return request.getParameter("code") != null;
    }
}
