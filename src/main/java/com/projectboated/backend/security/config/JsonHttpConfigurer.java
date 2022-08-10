package com.projectboated.backend.security.config;

import com.projectboated.backend.security.filter.JsonAuthenticationFilter;
import com.projectboated.backend.security.filter.KakaoAuthenticationFilter;
import com.projectboated.backend.security.handler.JsonAuthenticationFailureHandler;
import com.projectboated.backend.security.handler.JsonAuthenticationSuccessHandler;
import com.projectboated.backend.security.provider.JsonAuthenticationProvider;
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
