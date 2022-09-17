package com.projectboated.backend.web.config;

import com.projectboated.backend.account.account.entity.Account;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;

public class WithMockAccountContextFactory implements WithSecurityContextFactory<WithMockAccount> {
    @Override
    public SecurityContext createSecurityContext(WithMockAccount annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Account account = Account.builder()
                .id(annotation.id())
                .username(annotation.username())
                .nickname(annotation.nickname())
                .build();

        List<SimpleGrantedAuthority> roles = Arrays.stream(annotation.roles())
                .map(SimpleGrantedAuthority::new)
                .toList();

        context.setAuthentication(new UsernamePasswordAuthenticationToken(account, null, roles));

        return context;
    }
}
