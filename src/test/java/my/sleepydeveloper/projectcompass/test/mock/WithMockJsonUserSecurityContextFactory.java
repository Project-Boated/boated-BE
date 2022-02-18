package my.sleepydeveloper.projectcompass.test.mock;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.security.token.JsonAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockJsonUserSecurityContextFactory implements WithSecurityContextFactory<WithMockJsonUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockJsonUser annotation) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        JsonAuthenticationToken jsonAuthenticationToken = new JsonAuthenticationToken(
                new Account("username", "password", "nickname", "ROLE_USER"),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        context.setAuthentication(jsonAuthenticationToken);

        return context;
    }
}
