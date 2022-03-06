package my.sleepydeveloper.projectcompass.web.account.dto;

import my.sleepydeveloper.projectcompass.security.dto.UsernamePasswordDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountDto extends UsernamePasswordDto {

    @NotEmpty
    String nickname;

    public AccountDto(String username, String password, String nickname) {
        super(username, password);
        this.nickname = nickname;
    }
}
