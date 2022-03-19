package my.sleepydeveloper.projectcompass.security.code;

import my.sleepydeveloper.projectcompass.security.entrypoint.JsonAuthenticationEntryPoint;
import my.sleepydeveloper.projectcompass.security.handler.JsonAccessDeniedHandler;
import org.junit.jupiter.api.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(1)
public class SecurityHandlerTestConfig extends WebSecurityConfigurerAdapter {

    public SecurityHandlerTestConfig() {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/test/**")
                .authorizeRequests()
                .antMatchers("/test/access-denied").hasRole("Failure")
                .anyRequest().authenticated();

        http
                .csrf().disable();

        http
                .exceptionHandling()
                .authenticationEntryPoint(new JsonAuthenticationEntryPoint())
                .accessDeniedHandler(new JsonAccessDeniedHandler());
    }
}
