package my.sleepydeveloper.projectcompass.domain.account.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("KAKAO")
public class KakaoAccount extends Account{
	
	private Long kakaoId;

	@Builder
	public KakaoAccount(Long kakaoId, String profileUrl, String role) {
		super(null, null, null, profileUrl, role);
		this.kakaoId = kakaoId;
	}
}
