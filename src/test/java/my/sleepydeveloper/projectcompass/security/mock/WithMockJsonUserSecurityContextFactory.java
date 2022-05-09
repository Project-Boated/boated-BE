package my.sleepydeveloper.projectcompass.security.mock;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.Role;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.UploadFileProfileImage;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.UrlProfileImage;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import my.sleepydeveloper.projectcompass.security.token.JsonAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WithMockJsonUserSecurityContextFactory implements WithSecurityContextFactory<WithMockJsonUser> {

    private final AccountService accountService;

    public WithMockJsonUserSecurityContextFactory(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockJsonUser annotation) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Account account = new Account(annotation.username(),
                annotation.password(),
                annotation.nickname(),
                new UrlProfileImage(annotation.profileImageUrl()), Set.of(Role.USER));

        JsonAuthenticationToken jsonAuthenticationToken = new JsonAuthenticationToken(accountService.save(account),
                null,
                account.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList()));

        context.setAuthentication(jsonAuthenticationToken);

        return context;
    }
}
