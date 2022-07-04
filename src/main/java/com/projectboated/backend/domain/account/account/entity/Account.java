package com.projectboated.backend.domain.account.account.entity;

import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.common.entity.BaseTimeEntity;

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

    @Builder
    public Account(String username, String password, String nickname, ProfileImage profileImageFile, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImageFile;
        this.roles = roles;
    }

    public void changeProfile(@Nullable String nickname, @Nullable String password) {
        changeNickname(nickname);
        changePassword(password);
    }

    public void changeProfileImage(@Nullable ProfileImage profileImageFile) {
        this.profileImage = profileImageFile;
    }

    public void changePassword(@Nullable String password) {
        if (password != null) {
            this.password = password;
        }
    }

    public void changeNickname(@Nullable String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

    public void changeId(Long id) {
        if (id != null) {
            this.id = id;
        }
    }

}
