package my.sleepydeveloper.projectcompass.web.account.dto;

import lombok.Getter;

@Getter
public class NicknameUniqueValidationResponse {
	private boolean isDuplicated;
	
	public NicknameUniqueValidationResponse(boolean isDuplicated) {
		this.isDuplicated = isDuplicated;
	}
}
