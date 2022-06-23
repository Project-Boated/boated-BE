package org.projectboated.backend.domain.account.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.projectboated.backend.domain.common.entity.BaseTimeEntity;
import org.projectboated.backend.domain.profileimage.entity.ProfileImage;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name="account_role",
            joinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Account(String username, String password, String nickname, ProfileImage profileImageFile, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImageFile;
        this.roles = roles;
    }

    public void updateProfile(@Nullable String nickname, @Nullable String password) {
        updateNickname(nickname);
        updatePassword(password);
    }

    public void updateProfileImage(@Nullable ProfileImage profileImageFile) {
        this.profileImage = profileImageFile;
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
