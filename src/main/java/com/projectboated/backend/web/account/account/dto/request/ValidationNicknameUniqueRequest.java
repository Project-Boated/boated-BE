package com.projectboated.backend.web.account.account.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class ValidationNicknameUniqueRequest {

	@NotBlank
	@Size(min = 2, max = 15)
	private String nickname;

	@Builder
	public ValidationNicknameUniqueRequest(String nickname) {
		this.nickname = nickname;
	}
}
