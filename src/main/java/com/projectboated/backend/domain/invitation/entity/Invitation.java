package com.projectboated.backend.domain.invitation.entity;

import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.project.entity.Project;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) @Getter
public class Invitation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public Invitation(Account account, Project project) {
        this.account = account;
        this.project = project;
    }
}
