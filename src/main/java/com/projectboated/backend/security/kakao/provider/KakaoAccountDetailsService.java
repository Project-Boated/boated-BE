package com.projectboated.backend.security.kakao.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectboated.backend.account.account.entity.KakaoAccount;
import com.projectboated.backend.account.account.entity.Role;
import com.projectboated.backend.account.account.repository.KakaoAccountRepository;
import com.projectboated.backend.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.account.profileimage.entity.UrlProfileImage;
import com.projectboated.backend.account.profileimage.service.ProfileImageService;
import com.projectboated.backend.infra.kakao.KakaoWebService;
import com.projectboated.backend.security.kakao.provider.dto.KakaoAccountInformation;
import com.projectboated.backend.security.kakao.provider.dto.KakaoAuthenicationToken;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoAccountDetailsService {

    private final KakaoAccountRepository kakaoAccountRepository;
    private final ProfileImageService profileImageService;
    private final KakaoWebService kakaoWebService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoAccountLoginInform loadKakaoAccount(KakaoAuthenicationToken kakaoAuthenicationToken) throws
            UsernameNotFoundException,
            JsonProcessingException {
        String accessToken = kakaoAuthenicationToken.getAccessToken();
        String accountInformation = kakaoWebService.getAccountInformation(accessToken);
        KakaoAccountInformation accountInform = objectMapper.readValue(accountInformation,
                KakaoAccountInformation.class);

        Optional<KakaoAccount> byKakaoId = kakaoAccountRepository.findByKakaoId(accountInform.getId());
        if(byKakaoId.isPresent()) {
            return new KakaoAccountLoginInform(byKakaoId.get(), true);
        } else {
            ProfileImage profileImage = null;
            if (accountInform.getProfileImageUrl() != null) {
                profileImage = profileImageService.save(new UrlProfileImage(accountInform.getProfileImageUrl()));
            }

            KakaoAccount kakaoAccount = kakaoAccountRepository.save(KakaoAccount.kakaoBuilder()
                    .profileImageFile(profileImage)
                    .roles(Set.of(Role.USER))
                    .kakaoId(accountInform.getId())
                    .build());
            return new KakaoAccountLoginInform(kakaoAccount, false);
        }
    }

    @Getter
    public static class KakaoAccountLoginInform {
        private KakaoAccount account;
        private boolean isLogin;

        public KakaoAccountLoginInform(KakaoAccount account, boolean isLogin) {
            this.account = account;
            this.isLogin = isLogin;
        }
    }
}
