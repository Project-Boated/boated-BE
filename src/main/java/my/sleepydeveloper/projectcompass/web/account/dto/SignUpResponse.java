package my.sleepydeveloper.projectcompass.web.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpResponse {

    private Long id;

    public SignUpResponse(Long id) {
        this.id = id;
    }
}
