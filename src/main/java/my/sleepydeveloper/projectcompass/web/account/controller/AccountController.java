package my.sleepydeveloper.projectcompass.web.account.controller;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.service.AccountService;
import my.sleepydeveloper.projectcompass.domain.account.service.dto.AccountUpdateCondition;
import my.sleepydeveloper.projectcompass.web.value.AccountProfile;
import my.sleepydeveloper.projectcompass.security.dto.IdDto;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountDto;
import my.sleepydeveloper.projectcompass.web.account.dto.NicknameUniqueValidationRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.NicknameUniqueValidationResponse;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountProfileResponseDto;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountUpdateRequest;
import my.sleepydeveloper.projectcompass.web.account.dto.PutNicknameRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
	
	private final AccountService accountService;
	
	private final PasswordEncoder passwordEncoder;
	
	@PostMapping("/sign-up")
	public ResponseEntity<IdDto> signUp(@RequestBody @Validated AccountDto accountDto) {
		String username = accountDto.getUsername();
		String password = accountDto.getPassword();
		String nickname = accountDto.getNickname();
		String role = "ROLE_USER";
		
		Account savedAccount = accountService.save(new Account(username,
															   passwordEncoder.encode(password),
															   nickname,
															   role));
		
		return ResponseEntity.created(URI.create("/api/account/profile" + savedAccount.getId()))
							 .body(new IdDto(savedAccount.getId()));
	}
	
	@GetMapping("/profile")
	public ResponseEntity<AccountProfileResponseDto> getAccountProfile(@AuthenticationPrincipal Account account) {
		
		AccountProfile accountProfile = new AccountProfile(accountService.findById(account.getId()));
		
		return ResponseEntity.ok(new AccountProfileResponseDto(accountProfile));
	}
	
	@PatchMapping("/profile")
	public ResponseEntity<IdDto> updateAccountProfile(@AuthenticationPrincipal Account account,
													  @RequestBody @Validated
														  AccountUpdateRequest accountUpdateRequest) {
		
		AccountUpdateCondition updateCondition = AccountUpdateCondition.builder()
																	   .originalPassword(accountUpdateRequest.getOriginalPassword())
																	   .nickname(accountUpdateRequest.getNickname())
																	   .password(accountUpdateRequest.getPassword())
																	   .build();
		
		accountService.updateProfile(account, updateCondition);
		
		return ResponseEntity.ok(new IdDto(account.getId()));
	}
	
	@DeleteMapping("/profile")
	public ResponseEntity<IdDto> deleteAccount(@AuthenticationPrincipal Account account, HttpSession session) {
		Long accountId = account.getId();
		
		accountService.delete(account);
		session.invalidate();
		
		return ResponseEntity.ok(new IdDto(accountId));
	}
	
	@PostMapping("/profile/nickname/unique-validation")
	public ResponseEntity<NicknameUniqueValidationResponse> nicknameUniqueValidation(
		@Validated @RequestBody NicknameUniqueValidationRequest request) {
		
		return ResponseEntity.ok(new NicknameUniqueValidationResponse(accountService.isExistsNickname(request.getNickname())));
	}
    
    @PutMapping("/profile/nickname")
    public void putNickname (
			@Validated @RequestBody PutNicknameRequest request,
        @AuthenticationPrincipal Account account
    ) {
		AccountUpdateCondition accountUpdateCondition = new AccountUpdateCondition(request.getNickname(), null, null);
		accountService.updateProfile(account, accountUpdateCondition);
    }
}
