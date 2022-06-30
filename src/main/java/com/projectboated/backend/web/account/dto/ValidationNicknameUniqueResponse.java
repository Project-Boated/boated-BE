package com.projectboated.backend.web.account.dto;

import lombok.Getter;

@Getter
public class ValidationNicknameUniqueResponse {
	private boolean isDuplicated;
	
	public ValidationNicknameUniqueResponse(boolean isDuplicated) {
		this.isDuplicated = isDuplicated;
	}
}
