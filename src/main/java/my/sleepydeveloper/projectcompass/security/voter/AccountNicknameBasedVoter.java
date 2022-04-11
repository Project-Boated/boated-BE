package my.sleepydeveloper.projectcompass.security.voter;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.security.exception.NicknameRequiredException;

public class AccountNicknameBasedVoter implements AccessDecisionVoter {
	
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}
	
	@Override
	public int vote(Authentication authentication, Object object, Collection collection) {
		Account account = (Account)authentication.getPrincipal();
		
		if(StringUtils.hasText(account.getNickname())) {
			return ACCESS_ABSTAIN;
		}
		
		throw new NicknameRequiredException(ErrorCode.ACCOUNT_NICKNAME_REQUIRED);
	}
	
	@Override
	public boolean supports(Class clazz) {
		return true;
	}
}
