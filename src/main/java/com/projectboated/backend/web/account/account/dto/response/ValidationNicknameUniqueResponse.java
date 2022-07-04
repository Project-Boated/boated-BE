package com.projectboated.backend.web.account.account.dto.response;

import lombok.Getter;

@Getter
public class ValidationNicknameUniqueResponse {
	private boolean isDuplicated;
	
	public ValidationNicknameUniqueResponse(boolean isDuplicated) {
		this.isDuplicated = isDuplicated;
	}
}
