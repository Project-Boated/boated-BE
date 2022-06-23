package org.projectboated.backend.security.service;

import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.account.repository.AccountRepository;
import org.projectboated.backend.security.exception.IllegalUsernamePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class JsonUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalUsernamePassword("Username or Password is not Correct"));

        List<SimpleGrantedAuthority> simpleGrantedAuthorities = account.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).toList();
        return new AccountDetails(account, simpleGrantedAuthorities);
    }
}
