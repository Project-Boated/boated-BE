package com.projectboated.backend.account.account.entity;

import com.projectboated.backend.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "oauth_site")
@DiscriminatorValue("null")
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String nickname;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Builder
    public Account(Long id, String username, String password, String nickname, ProfileImage profileImageFile, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImageFile;
        this.roles = roles;
    }

    public void changeProfileImage(ProfileImage profileImageFile) {
        this.profileImage = profileImageFile;
    }

    public void changePassword(String password) {
        if (password != null) {
            this.password = password;
        }
    }

    public void changeNickname(String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

}
