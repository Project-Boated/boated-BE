package my.sleepydeveloper.projectcompass.domain.account.entity;

import lombok.Builder;
import my.sleepydeveloper.projectcompass.domain.common.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter @NoArgsConstructor
@Builder
public class Account extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String role;

    public Account(String username, String password, String nickname, String role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public Account(Long id, String username, String password, String nickname, String role) {
        this(username, password, nickname, role);
        this.id = id;
    }

    public void updateProfile(String nickname, String password) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (password != null) {
            this.password = password;
        }
    }

}
