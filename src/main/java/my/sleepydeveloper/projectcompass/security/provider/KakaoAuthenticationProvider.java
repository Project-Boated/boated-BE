package my.sleepydeveloper.projectcompass.security.provider;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.KakaoAccount;
import my.sleepydeveloper.projectcompass.security.exception.JsonParsingException;
import my.sleepydeveloper.projectcompass.security.provider.dto.KakaoTokenResponse;
import my.sleepydeveloper.projectcompass.security.service.KakaoAccountDetailsService;
import my.sleepydeveloper.projectcompass.security.service.KakaoWebService;
import my.sleepydeveloper.projectcompass.security.token.KakaoAuthenticationToken;

@Component
@RequiredArgsConstructor
public class KakaoAuthenticationProvider implements AuthenticationProvider {

    private final KakaoAccountDetailsService kakaoUserDetailsService;
    private final KakaoWebService kakaoWebService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    
        try {
            String accessToken = (String)authentication.getPrincipal();

            KakaoAccount account = kakaoUserDetailsService.loadKakaoAccountByAccessToken(accessToken);
            List<SimpleGrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(account.getRole()));

            return new KakaoAuthenticationToken(account, null, grantedAuthorities);
        } catch (JsonProcessingException e) {
            throw new JsonParsingException("Json ParsingError");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(KakaoAuthenticationToken.class);
    }
}
