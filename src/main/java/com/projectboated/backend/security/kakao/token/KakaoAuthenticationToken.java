package com.projectboated.backend.security.kakao.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class KakaoAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;

    private Object credentials;

    private boolean isLogin;

    public KakaoAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public KakaoAuthenticationToken(Object principal, Object credentials,
                                    Collection<? extends GrantedAuthority> authorities,
                                    boolean isLogin) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.isLogin = isLogin;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
