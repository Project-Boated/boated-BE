package com.projectboated.backend.security.service;

import com.projectboated.backend.account.account.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AccountDetails extends User {

    private Account account;

    public AccountDetails(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
