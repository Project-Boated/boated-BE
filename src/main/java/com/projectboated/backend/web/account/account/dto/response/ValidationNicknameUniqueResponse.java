package com.projectboated.backend.web.account.account.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ValidationNicknameUniqueResponse {
	private boolean isDuplicated;

	@Builder
	public ValidationNicknameUniqueResponse(boolean isDuplicated) {
		this.isDuplicated = isDuplicated;
	}
}
