package com.projectboated.backend.security.kakao.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.infra.kakao.KakaoWebService;
import com.projectboated.backend.security.common.exception.JsonParsingException;
import com.projectboated.backend.security.kakao.provider.dto.KakaoAccount;
import com.projectboated.backend.security.kakao.provider.dto.KakaoAuthenicationToken;
import com.projectboated.backend.security.kakao.provider.exception.KakaoServerException;
import com.projectboated.backend.security.kakao.token.KakaoAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.stream.Collectors;

import static com.projectboated.backend.security.kakao.provider.KakaoAccountDetailsService.*;

@RequiredArgsConstructor
@Component
public class KakaoAuthenticationProvider implements AuthenticationProvider {

    private final KakaoAccountDetailsService kakaoUserDetailsService;
    private final KakaoWebService kakaoWebService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String authenticationToken = kakaoWebService.getToken((String) authentication.getPrincipal());
            KakaoAuthenicationToken kakaoAuthenticationToken = objectMapper.readValue(authenticationToken, KakaoAuthenicationToken.class);

            KakaoAccountLoginInform accountLoginInform = kakaoUserDetailsService.loadKakaoAccount(kakaoAuthenticationToken);

            Account account = accountLoginInform.getAccount();
            List<SimpleGrantedAuthority> grantedAuthorities = account.getRoles().stream()
                    .map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());

            kakaoWebService.logout(kakaoAuthenticationToken);

            return new KakaoAuthenticationToken(account, null, grantedAuthorities, accountLoginInform.isLogin());
        } catch (JsonProcessingException e) {
            throw new JsonParsingException();
        } catch (WebClientResponseException e) {
            throw new KakaoServerException();
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(KakaoAuthenticationToken.class);
    }
}
