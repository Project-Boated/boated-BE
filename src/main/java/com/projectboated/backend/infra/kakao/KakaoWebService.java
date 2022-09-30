package com.projectboated.backend.infra.kakao;

import com.projectboated.backend.infra.kakao.exception.KakaoTimeoutException;
import com.projectboated.backend.security.kakao.provider.dto.KakaoAuthenicationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class KakaoWebService {

    @Value("${kakao.clientId}")
    String clientId;

    @Value("${kakao.redirectUri}")
    String redirectUri;

    @Value("${kakao.clientSecret}")
    String clientSecret;

    @Value("${kakao.adminKey}")
    String adminKey;

    private Long timeoutMills = 2000L;

    private final WebClient authClient;
    private final WebClient apiClient;

    public KakaoWebService() {
        this.authClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .build();

        this.apiClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build();
    }

    public String getToken(String code) {
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("grant_type", "authorization_code");
        bodyMap.add("client_id", clientId);
        bodyMap.add("redirect_uri", redirectUri);
        bodyMap.add("code", code);
        bodyMap.add("client_secret", clientSecret);

        return authClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(bodyMap))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(timeoutMills), Mono.error(new KakaoTimeoutException("timeout")))
                .block();
    }

    public String getAccountInformation(String token) {
        return apiClient.get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(timeoutMills), Mono.error(new KakaoTimeoutException("timeout")))
                .block();
    }

    public void logout(KakaoAuthenicationToken kakaoAuthenicationToken) {
        apiClient.get()
                .uri("/v1/user/logout")
                .header("Authorization", "Bearer " + kakaoAuthenicationToken.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(timeoutMills), Mono.error(new KakaoTimeoutException("timeout")))
                .block();
    }

    public void disconnect(Long kakaoId) {
        apiClient.post()
                .uri("/v1/user/unlink?target_id_type=user_id&target_id=" + kakaoId)
                .header("Authorization", "KakaoAK " + adminKey)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofMillis(timeoutMills), Mono.error(new KakaoTimeoutException("timeout")))
                .block();
    }

}
