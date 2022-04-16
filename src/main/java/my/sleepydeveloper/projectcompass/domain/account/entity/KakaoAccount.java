package my.sleepydeveloper.projectcompass.domain.account.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("KAKAO")
public class KakaoAccount extends Account{

	@Column(unique = true)
	private Long kakaoId;

	@Builder
	public KakaoAccount(Long kakaoId, Set<Role> roles, String profileUrl) {
		super(null, null, null, profileUrl, roles);
		this.kakaoId = kakaoId;
	}
}
