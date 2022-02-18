package my.sleepydeveloper.projectcompass.security.config;

import my.sleepydeveloper.projectcompass.security.entrypoint.JsonAuthenticationEntryPoint;
import my.sleepydeveloper.projectcompass.security.handler.JsonAccessDeniedHandler;
import my.sleepydeveloper.projectcompass.security.handler.JsonLogoutSuccessHandler;
import org.junit.jupiter.api.Order;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(0)
public class SecurityTestConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/test/**")
                .authorizeRequests()
                .antMatchers("/test/access-test").hasRole("TEST")
                .anyRequest().authenticated();

        http
                .csrf().disable();

        http
                .exceptionHandling()
                .authenticationEntryPoint(new JsonAuthenticationEntryPoint())
                .accessDeniedHandler(new JsonAccessDeniedHandler());
    }
}
