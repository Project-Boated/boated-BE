package org.projectboated.backend.web.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
public class PutNicknameRequest {

	@NotEmpty
	private String nickname;

	public PutNicknameRequest(String nickname) {
		this.nickname = nickname;
	}
}