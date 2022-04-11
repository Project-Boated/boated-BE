package my.sleepydeveloper.projectcompass.security.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.KakaoAccount;
import my.sleepydeveloper.projectcompass.domain.account.repository.KakaoAccountRepository;
import my.sleepydeveloper.projectcompass.security.service.dto.KakaoAccountResponse;

@Component
@RequiredArgsConstructor
@Transactional
public class KakaoAccountDetailsService {
	
	private final KakaoAccountRepository kakaoAccountRepository;
	private final KakaoWebService kakaoWebService;
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public KakaoAccount loadKakaoAccountByAccessToken(String accessToken) throws
																		  UsernameNotFoundException,
																		  JsonProcessingException {
		
		String tokenInformationResponse = kakaoWebService.getAccountInformation(accessToken);
		kakaoWebService.logout(accessToken);
		
		KakaoAccountResponse kakaoAccountResponse = objectMapper.readValue(tokenInformationResponse,
																		   KakaoAccountResponse.class);
		
		return kakaoAccountRepository.findByKakaoId(kakaoAccountResponse.getId())
									 .orElseGet(() -> kakaoAccountRepository.save(KakaoAccount.builder()
																							  .profileUrl(
																								  kakaoAccountResponse.getKakaoAccount()
																													  .getProfile()
																													  .getThumbnailImageUrl())
																							  .role("ROLE_USER")
																							  .kakaoId(
																								  kakaoAccountResponse.getId())
																							  .build()));
	}
}
