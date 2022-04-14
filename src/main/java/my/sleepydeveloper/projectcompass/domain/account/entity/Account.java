package my.sleepydeveloper.projectcompass.domain.account.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.common.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="oauth_site")
public class Account extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String nickname;
    
    private String profileUrl;

    private String role;
    
    public Account(String username, String password, String nickname, String role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public Account(String username, String password, String nickname, String profileUrl, String role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.role = role;
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
