package com.projectboated.backend.security.config;

import com.projectboated.backend.security.filter.KakaoAuthenticationFilter;
import com.projectboated.backend.security.handler.KakaoAuthenticationFailureHandler;
import com.projectboated.backend.security.handler.KakaoAuthenticationSuccessHandler;
import com.projectboated.backend.security.provider.KakaoAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoHttpConfigurer extends AbstractHttpConfigurer<KakaoHttpConfigurer, HttpSecurity> {

    private final KakaoAuthenticationProvider kakaoAuthenticationProvider;
    private final KakaoAuthenticationSuccessHandler successHandler;
    private final KakaoAuthenticationFailureHandler failureHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authenticationProvider(kakaoAuthenticationProvider);
        http.addFilterBefore(kakaoAuthenticationFilter(http), UsernamePasswordAuthenticationFilter.class);
    }

    public KakaoAuthenticationFilter kakaoAuthenticationFilter(HttpSecurity http) {
        KakaoAuthenticationFilter kakaoAuthenticationFilter = new KakaoAuthenticationFilter();
        kakaoAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        kakaoAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        kakaoAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);
        return kakaoAuthenticationFilter;
    }
}
