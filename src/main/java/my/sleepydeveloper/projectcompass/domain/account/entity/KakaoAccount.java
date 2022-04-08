package my.sleepydeveloper.projectcompass.domain.account.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("KAKAO")
public class KakaoAccount extends Account{
	
	private Long kakaoId;
	
	@Builder
	public KakaoAccount(Long id, String username, String password, String nickname, String profileUrl, String role, Long kakaoId) {
		super(id, username, password, nickname, profileUrl, role);
		this.kakaoId = kakaoId;
	}
}
