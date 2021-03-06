package com.projectboated.backend.domain.invitation.entity;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.common.entity.BaseEntity;
import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.project.entity.Project;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Invitation(Account account, Project project) {
        this.account = account;
        this.project = project;
    }
}
