package org.projectboated.backend.security.config;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.security.entrypoint.JsonAuthenticationEntryPoint;
import org.projectboated.backend.security.filter.JsonAuthenticationFilter;
import org.projectboated.backend.security.filter.KakaoAuthenticationFilter;
import org.projectboated.backend.security.handler.*;
import org.projectboated.backend.security.provider.JsonAuthenticationProvider;
import org.projectboated.backend.security.provider.KakaoAuthenticationProvider;
import org.projectboated.backend.security.voter.AccountNicknameExistVoter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JsonAuthenticationProvider jsonAuthenticationProvider;
    private final KakaoAuthenticationProvider kakaoAuthenticationProvider;
    private final AccountNicknameExistVoter accountNicknameExistVoter;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .antMatchers("/docs/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jsonAuthenticationProvider);
        auth.authenticationProvider(kakaoAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .accessDecisionManager(accessDecisionManager())
                .antMatchers("/api/sign-in/kakao").permitAll()
                .antMatchers("/api/account/sign-up").permitAll()
                .antMatchers("/test/**").permitAll()
                .anyRequest().authenticated();

        http
                .addFilterBefore(characterEncodingFilter(), CsrfFilter.class)
                .addFilterBefore(kakaoAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jsonAuthenticationFilter(), KakaoAuthenticationFilter.class);

        http
                .csrf().disable();

        http
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new JsonLogoutSuccessHandler())
                .permitAll();

        http
                .exceptionHandling()
                .authenticationEntryPoint(new JsonAuthenticationEntryPoint())
                .accessDeniedHandler(new JsonAccessDeniedHandler());
    }

    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter();
        jsonAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        jsonAuthenticationFilter.setAuthenticationSuccessHandler(new JsonAuthenticationSuccessHandler());
        jsonAuthenticationFilter.setAuthenticationFailureHandler(new JsonAuthenticationFailureHandler());
        return jsonAuthenticationFilter;
    }
    
    @Bean
    public KakaoAuthenticationFilter kakaoAuthenticationFilter() throws Exception {
        KakaoAuthenticationFilter kakaoAuthenticationFilter = new KakaoAuthenticationFilter();
        kakaoAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        kakaoAuthenticationFilter.setAuthenticationSuccessHandler(new KakaoAuthenticationSuccessHandler());
        kakaoAuthenticationFilter.setAuthenticationFailureHandler(new KakaoAuthenticationFailureHandler());
        return kakaoAuthenticationFilter;
    }
    
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = Arrays.asList(
            accountNicknameExistVoter,
            new WebExpressionVoter()
        );
        return new AffirmativeBased(decisionVoters);
    }
    
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        return new CharacterEncodingFilter("UTF-8", true);
    }
}