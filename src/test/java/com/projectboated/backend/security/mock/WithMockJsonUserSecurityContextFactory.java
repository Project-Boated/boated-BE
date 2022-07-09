package com.projectboated.backend.security.mock;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.entity.Role;
import com.projectboated.backend.domain.account.account.service.AccountService;
import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.account.profileimage.entity.UrlProfileImage;
import com.projectboated.backend.domain.account.profileimage.service.ProfileImageService;
import com.projectboated.backend.security.token.JsonAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static com.projectboated.backend.common.data.BasicAccountData.ACCOUNT_ID;

@Component
public class WithMockJsonUserSecurityContextFactory implements WithSecurityContextFactory<WithMockJsonUser> {

    private final AccountService accountService;
    private final ProfileImageService profileImageService;

    public WithMockJsonUserSecurityContextFactory(AccountService accountService, ProfileImageService profileImageService) {
        this.accountService = accountService;
        this.profileImageService = profileImageService;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockJsonUser annotation) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        ProfileImage profileImage = profileImageService.save(new UrlProfileImage(annotation.profileImageUrl()));

        Account account = new Account(ACCOUNT_ID, annotation.username(),
                annotation.password(),
                annotation.nickname(),
                profileImage,
                Set.of(Role.USER));

        JsonAuthenticationToken jsonAuthenticationToken = new JsonAuthenticationToken(accountService.save(account),
                null,
                account.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList()));

        context.setAuthentication(jsonAuthenticationToken);

        return context;
    }
}
