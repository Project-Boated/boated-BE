package my.sleepydeveloper.projectcompass.security.config;

import my.sleepydeveloper.projectcompass.security.entrypoint.JsonAuthenticationEntryPoint;
import my.sleepydeveloper.projectcompass.security.filter.JsonAuthenticationFilter;
import my.sleepydeveloper.projectcompass.security.handler.JsonAccessDeniedHandler;
import my.sleepydeveloper.projectcompass.security.handler.JsonAuthenticationFailureHandler;
import my.sleepydeveloper.projectcompass.security.handler.JsonAuthenticationSuccessHandler;
import my.sleepydeveloper.projectcompass.security.handler.JsonLogoutSuccessHandler;
import my.sleepydeveloper.projectcompass.security.provider.JsonAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JsonAuthenticationProvider jsonAuthenticationProvider;

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
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/account/sign-up").permitAll()
                .anyRequest().authenticated();

        http
                .addFilterBefore(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

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
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
