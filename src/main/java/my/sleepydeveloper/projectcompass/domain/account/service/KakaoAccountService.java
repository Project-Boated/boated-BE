package my.sleepydeveloper.projectcompass.domain.account.service;

import my.sleepydeveloper.projectcompass.security.service.KakaoWebService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.KakaoAccount;
import my.sleepydeveloper.projectcompass.domain.account.repository.KakaoAccountRepository;
import my.sleepydeveloper.projectcompass.security.service.dto.KakaoAccountInformation;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoAccountService {

    private final KakaoAccountRepository kakaoAccountRepository;
    private final KakaoWebService kakaoWebService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoAccount loadKakaoAccountByAccessToken(String accessToken) throws
            UsernameNotFoundException,
            JsonProcessingException {

        String accountInformation = kakaoWebService.getAccountInformation(accessToken);
        kakaoWebService.logout(accessToken);

        KakaoAccountInformation kakaoAccountResponse = objectMapper.readValue(accountInformation,
                KakaoAccountInformation.class);

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
