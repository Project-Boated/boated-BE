package my.sleepydeveloper.projectcompass.security.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoAccountInformation {
    private Long id;
    private String connectedAt;
    private KakaoProperties properties;
    private KakaoAccount kakaoAccount;

    public String getProfileImageUrl() {
        return kakaoAccount.getProfile().getIsDefaultImage().equals("true") ? null : getKakaoAccount().getProfile().getProfileImageUrl();
    }
}
