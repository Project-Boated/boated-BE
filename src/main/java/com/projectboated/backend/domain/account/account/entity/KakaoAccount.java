package com.projectboated.backend.domain.account.account.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("KAKAO")
public class KakaoAccount extends Account {

	@Column(name = "oauth_id", unique = true)
	private Long kakaoId;

	@Builder(builderMethodName = "kakaoBuilder")
	public KakaoAccount(Long kakaoId, Set<Role> roles, ProfileImage profileImageFile) {
		super(null, null, null, null, profileImageFile, roles);
		this.kakaoId = kakaoId;
	}
}
