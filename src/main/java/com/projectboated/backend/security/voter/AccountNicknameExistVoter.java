package com.projectboated.backend.security.voter;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.service.AccountService;
import com.projectboated.backend.security.voter.exception.NicknameRequiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AccountNicknameExistVoter implements AccessDecisionVoter {

    private final AccountService accountService;

    private static Map<String, Set<String>> urlWhitelist = new HashMap<>();

    static {
        urlWhitelist.put("/api/account/profile/nickname", Set.of("PUT"));
        urlWhitelist.put("/api/account/profile/nickname/unique-validation", Set.of("POST"));
        urlWhitelist.put("/api/account/profile", Set.of("GET", "PATCH"));
        urlWhitelist.put("/api/account/profile/profile-image", Set.of("GET", "POST"));
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {
        if (authentication.getPrincipal() instanceof Account) {
            FilterInvocation filterInvocation = (FilterInvocation) object;
            String requestURI = filterInvocation.getHttpRequest().getRequestURI();
            String method = filterInvocation.getHttpRequest().getMethod();
            
            if (isInWhiteList(requestURI, method)) {
                return ACCESS_ABSTAIN;
            }

            Account account = accountService.findById(((Account) authentication.getPrincipal()).getId());
            if (account.getNickname().isBlank()) {
                throw new NicknameRequiredException();
            }
        }
        return ACCESS_ABSTAIN;
    }

    private boolean isInWhiteList(String requestURI, String method) {
        return urlWhitelist.containsKey(requestURI) && urlWhitelist.get(requestURI).contains(method);
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }
}
