package my.sleepydeveloper.projectcompass.security.voter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.security.exception.NicknameRequiredException;

@Component
public class AccountNicknameExistVoter implements AccessDecisionVoter {
	
	private final AccountService accountService;
	
	private static Map<String, String> urlWhitelist = new HashMap<>();
	static {
		urlWhitelist.put("/api/account/profile/nickname", "PUT");
		urlWhitelist.put("/api/account/profile/nickname/unique-validation", "POST");
	}
	
	public AccountNicknameExistVoter(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}
	
	@Override
	public int vote(Authentication authentication, Object object, Collection collection) {
		if(authentication.getPrincipal() instanceof Account) {
			FilterInvocation filterInvocation = (FilterInvocation)object;
			String requestURI = filterInvocation.getHttpRequest()
												.getRequestURI();
			String method = filterInvocation.getHttpRequest()
											.getMethod();
			if(isInWhiteList(requestURI, method)) {
				return ACCESS_ABSTAIN;
			}
			
			Account account = accountService.findById(((Account)authentication.getPrincipal()).getId());
			
			if(!StringUtils.hasText(account.getNickname())) {
				throw new NicknameRequiredException(ErrorCode.ACCOUNT_NICKNAME_REQUIRED);
			}
		}
		return ACCESS_ABSTAIN;
	}
	
	private boolean isInWhiteList(String requestURI, String method) {
		return urlWhitelist.containsKey(requestURI) && urlWhitelist.get(requestURI)
																   .equals(method);
	}
	
	@Override
	public boolean supports(Class clazz) {
		return true;
	}
}
