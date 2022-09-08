package com.projectboated.backend.security.config;

import com.projectboated.backend.security.entrypoint.JsonAuthenticationEntryPoint;
import com.projectboated.backend.security.handler.JsonAccessDeniedHandler;
import com.projectboated.backend.security.handler.JsonLogoutSuccessHandler;
import com.projectboated.backend.security.voter.AccountNicknameExistVoter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AccountNicknameExistVoter accountNicknameExistVoter;
    private final JsonLogoutSuccessHandler logoutSuccessHandler;
    private final JsonAuthenticationEntryPoint authenticationEntryPoint;
    private final JsonAccessDeniedHandler accessDeniedHandler;

    private final JsonHttpConfigurer jsonConfigurer;
    private final KakaoHttpConfigurer kakaoHttpConfigurer;
    private final CharacterEncodingHttpConfigurer characterEncodingHttpConfigurer;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .antMatchers("/docs/**", "/error", "robots.txt");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .accessDecisionManager(accessDecisionManager())
                .antMatchers("/api/sign-in/kakao").permitAll()
                .antMatchers("/api/account/sign-up").permitAll()
                .antMatchers("/test/**").permitAll()
                .anyRequest().authenticated();

        http.csrf()
                .disable();

        http.logout()
                .logoutUrl("/api/account/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();

        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        http.requestCache()
                .disable();

        http.cors()
                .configurationSource(corsConfigurationSource());

        http.apply(kakaoHttpConfigurer);
        http.apply(jsonConfigurer);
        http.apply(characterEncodingHttpConfigurer);

        return http.build();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new AffirmativeBased(List.of(accountNicknameExistVoter, new WebExpressionVoter()));
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://15.164.89.188");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.HEAD);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.PATCH);
        configuration.addAllowedMethod(HttpMethod.DELETE);
        configuration.addAllowedMethod(HttpMethod.OPTIONS);
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
