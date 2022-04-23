package my.sleepydeveloper.projectcompass.domain.account.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.common.entity.BaseTimeEntity;
import my.sleepydeveloper.projectcompass.domain.uploadfile.UploadFile;

import javax.annotation.Nullable;
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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_image_file_id")
    private UploadFile profileImageFile;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="account_role",
            joinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Account(String username, String password, String nickname, UploadFile profileImageFile, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImageFile = profileImageFile;
        this.roles = roles;
    }

    public void updateProfile(@Nullable String nickname, @Nullable String password, UploadFile uploadFile) {
        updateNickname(nickname);
        updatePassword(password);
        updateProfileImageFile(uploadFile);
    }

    public void updateProfileImageFile(@Nullable UploadFile profileImageFile) {
        if (profileImageFile != null) {
            this.profileImageFile = profileImageFile;
        }
    }

    public void updatePassword(@Nullable String password) {
        if (password != null) {
            this.password = password;
        }
    }

    public void updateNickname(@Nullable String nickname) {
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
