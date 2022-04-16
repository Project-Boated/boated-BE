package my.sleepydeveloper.projectcompass.domain.account.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.common.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="oauth_site")
@DiscriminatorValue("null")
public class Account extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String nickname;
    
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="account_role",
            joinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Account(String username, String password, String nickname, String profileImageUrl, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.roles = roles;
    }

    public void updateProfile(String nickname, String password, String profileImageUrl) {
        updateNickname(nickname);
        updatePassword(password);
        updateProfileImageUrl(profileImageUrl);
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }

    public void updatePassword(String password) {
        if (password != null) {
            this.password = password;
        }
    }

    public void updateNickname(String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

    public void updateId(Long id) {
        if (id != null) {
            this.id = id;
        }
    }

}
