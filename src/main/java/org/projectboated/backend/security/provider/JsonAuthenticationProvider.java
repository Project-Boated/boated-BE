package org.projectboated.backend.security.provider;

import org.projectboated.backend.security.exception.IllegalUsernamePassword;
import org.projectboated.backend.security.service.AccountDetails;
import org.projectboated.backend.security.service.JsonUserDetailsService;
import org.projectboated.backend.security.token.JsonAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JsonAuthenticationProvider implements AuthenticationProvider {

    private final JsonUserDetailsService jsonUserDetailsService;
    private final ObjectProvider<PasswordEncoder> passwordEncoderProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PasswordEncoder passwordEncoder = passwordEncoderProvider.getIfAvailable();

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        AccountDetails accountDetails = (AccountDetails) jsonUserDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, accountDetails.getPassword())) {
            throw new IllegalUsernamePassword("Username or Password is not Correct");
        }

        return new JsonAuthenticationToken(accountDetails.getAccount(), null, accountDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JsonAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
