package com.projectboated.backend.security.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectboated.backend.account.account.entity.KakaoAccount;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.infra.kakao.KakaoWebService;
import com.projectboated.backend.security.exception.JsonParsingException;
import com.projectboated.backend.security.exception.KakaoServerException;
import com.projectboated.backend.security.provider.dto.KakaoTokenResponse;
import com.projectboated.backend.security.service.KakaoAccountDetailsService;
import com.projectboated.backend.security.token.KakaoAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoAuthenticationProvider implements AuthenticationProvider {

    private final KakaoAccountDetailsService kakaoUserDetailsService;
    private final KakaoWebService kakaoWebService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String tokenJson = kakaoWebService.getToken((String) authentication.getPrincipal());
            KakaoTokenResponse kakaoTokenResponse = objectMapper.readValue(tokenJson, KakaoTokenResponse.class);
            String accessToken = kakaoTokenResponse.getAccessToken();

            KakaoAccount account = kakaoUserDetailsService.loadKakaoAccountByAccessToken(accessToken);
            List<SimpleGrantedAuthority> grantedAuthorities = account.getRoles().stream()
                    .map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());

            return new KakaoAuthenticationToken(account, null, grantedAuthorities);
        } catch (JsonProcessingException e) {
            throw new JsonParsingException("Json ParsingError");
        } catch (WebClientResponseException e) {
            log.error("kakao server exception", e);
            throw new KakaoServerException(ErrorCode.KAKAO_SERVER_EXCEPTION);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(KakaoAuthenticationToken.class);
    }
}
