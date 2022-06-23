package org.projectboated.backend.domain.account.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.projectboated.backend.domain.profileimage.entity.ProfileImage;

import java.util.Set;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("KAKAO")
public class KakaoAccount extends Account{

	@Column(name = "oauth_id", unique = true)
	private Long kakaoId;

	@Builder
	public KakaoAccount(Long kakaoId, Set<Role> roles, ProfileImage profileImageFile) {
		super(null, null, null, profileImageFile, roles);
		this.kakaoId = kakaoId;
	}
}