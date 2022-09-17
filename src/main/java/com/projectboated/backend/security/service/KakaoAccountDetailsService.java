package com.projectboated.backend.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectboated.backend.account.account.entity.KakaoAccount;
import com.projectboated.backend.account.account.entity.Role;
import com.projectboated.backend.account.account.repository.KakaoAccountRepository;
import com.projectboated.backend.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.account.profileimage.entity.UrlProfileImage;
import com.projectboated.backend.account.profileimage.service.ProfileImageService;
import com.projectboated.backend.security.service.dto.KakaoAccountInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoAccountDetailsService {

    private final KakaoAccountRepository kakaoAccountRepository;
    private final ProfileImageService profileImageService;
    private final com.projectboated.backend.security.service.KakaoWebService kakaoWebService;
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
                .orElseGet(() -> {
                    ProfileImage profileImage = null;
                    if (kakaoAccountResponse.getProfileImageUrl() != null) {
                        profileImage = profileImageService.save(new UrlProfileImage(kakaoAccountResponse.getProfileImageUrl()));
                    }

                    return kakaoAccountRepository.save(KakaoAccount.kakaoBuilder()
                            .profileImageFile(profileImage)
                            .roles(Set.of(Role.USER))
                            .kakaoId(
                                    kakaoAccountResponse.getId())
                            .build());
                });
    }
}
