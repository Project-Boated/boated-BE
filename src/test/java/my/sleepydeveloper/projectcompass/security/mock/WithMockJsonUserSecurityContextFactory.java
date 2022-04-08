package my.sleepydeveloper.projectcompass.security.mock;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.security.token.JsonAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WithMockJsonUserSecurityContextFactory implements WithSecurityContextFactory<WithMockJsonUser> {
	
	private final AccountService accountService;
	private final PasswordEncoder passwordEncoder;
	
	public WithMockJsonUserSecurityContextFactory(AccountService accountService, PasswordEncoder passwordEncoder) {
		this.accountService = accountService;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public SecurityContext createSecurityContext(WithMockJsonUser annotation) {
		
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		
		Account account = new Account(annotation.username(),
									  passwordEncoder.encode(annotation.password()),
									  annotation.nickname(),
									  annotation.role());
		
		JsonAuthenticationToken jsonAuthenticationToken = new JsonAuthenticationToken(accountService.save(account),
																					  null,
																					  List.of(new SimpleGrantedAuthority(
																						  account.getRole())));
		
		context.setAuthentication(jsonAuthenticationToken);
		
		return context;
	}
}
