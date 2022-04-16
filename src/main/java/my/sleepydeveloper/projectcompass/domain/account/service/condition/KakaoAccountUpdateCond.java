package my.sleepydeveloper.projectcompass.domain.account.service.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoAccountUpdateCond {

    private String nickname;
    private String profileUrl;
}
