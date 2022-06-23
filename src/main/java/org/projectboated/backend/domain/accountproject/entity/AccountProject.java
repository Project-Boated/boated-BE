package org.projectboated.backend.domain.accountproject.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.project.entity.Project;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountProject {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_project_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public AccountProject(Account account, Project project) {
        this.account = account;
        this.project = project;
    }
}
