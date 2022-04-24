package my.sleepydeveloper.projectcompass.security.service;

import my.sleepydeveloper.projectcompass.domain.account.entity.Role;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.KakaoAccount;
import my.sleepydeveloper.projectcompass.domain.account.repository.KakaoAccountRepository;
import my.sleepydeveloper.projectcompass.security.service.dto.KakaoAccountInformation;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoAccountDetailsService {

    private final KakaoAccountRepository kakaoAccountRepository;
    private final KakaoWebService kakaoWebService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public KakaoAccount loadKakaoAccountByAccessToken(String accessToken) throws
            UsernameNotFoundException,
            JsonProcessingException {

        String accountInformation = kakaoWebService.getAccountInformation(accessToken);
        kakaoWebService.logout(accessToken);

        KakaoAccountInformation kakaoAccountResponse = objectMapper.readValue(accountInformation,
                KakaoAccountInformation.class);

        return kakaoAccountRepository.findByKakaoId(kakaoAccountResponse.getId())
                .orElseGet(() -> kakaoAccountRepository.save(KakaoAccount.builder()
                        .profileImageFile(kakaoAccountResponse.getProfileImageUrl()==null ? null : new UploadFile(kakaoAccountResponse.getProfileImageUrl()))
                        .roles(Set.of(Role.USER))
                        .kakaoId(
                                kakaoAccountResponse.getId())
                        .build()));
    }
}
