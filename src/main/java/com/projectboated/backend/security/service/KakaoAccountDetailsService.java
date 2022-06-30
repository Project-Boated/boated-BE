package com.projectboated.backend.security.service;

import com.projectboated.backend.domain.account.entity.KakaoAccount;
import com.projectboated.backend.domain.account.entity.Role;
import com.projectboated.backend.security.service.dto.KakaoAccountInformation;
import com.projectboated.backend.domain.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.profileimage.entity.UrlProfileImage;
import com.projectboated.backend.domain.profileimage.service.ProfileImageService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.account.repository.KakaoAccountRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoAccountDetailsService {

    private final KakaoAccountRepository kakaoAccountRepository;
    private final ProfileImageService profileImageService;
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
                .orElseGet(() -> {
                    ProfileImage profileImage = null;
                    if (kakaoAccountResponse.getProfileImageUrl() != null) {
                        profileImage = profileImageService.save(new UrlProfileImage(kakaoAccountResponse.getProfileImageUrl()));
                    }

                    return kakaoAccountRepository.save(KakaoAccount.builder()
                            .profileImageFile(profileImage)
                            .roles(Set.of(Role.USER))
                            .kakaoId(
                                    kakaoAccountResponse.getId())
                            .build());
                });
    }
}
