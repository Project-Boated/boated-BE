package my.sleepydeveloper.projectcompass.web.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
public class PutNicknameRequest {

	@NotEmpty
	private String nickname;
}
