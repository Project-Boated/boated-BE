package com.projectboated.backend.security.json.config;

import com.projectboated.backend.security.json.filter.JsonAuthenticationFilter;
import com.projectboated.backend.security.kakao.filter.KakaoAuthenticationFilter;
import com.projectboated.backend.security.json.handler.JsonAuthenticationFailureHandler;
import com.projectboated.backend.security.json.handler.JsonAuthenticationSuccessHandler;
import com.projectboated.backend.security.json.provider.JsonAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonHttpConfigurer extends AbstractHttpConfigurer<JsonHttpConfigurer, HttpSecurity> {

    private final JsonAuthenticationProvider jsonAuthenticationProvider;
    private final JsonAuthenticationSuccessHandler successHandler;
    private final JsonAuthenticationFailureHandler failureHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authenticationProvider(jsonAuthenticationProvider);
        http.addFilterBefore(jsonAuthenticationFilter(http), KakaoAuthenticationFilter.class);
    }

    public JsonAuthenticationFilter jsonAuthenticationFilter(HttpSecurity http) throws Exception {
        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter();
        jsonAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        jsonAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        jsonAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);
        return jsonAuthenticationFilter;
    }
}
