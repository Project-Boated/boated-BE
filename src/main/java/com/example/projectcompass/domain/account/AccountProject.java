package com.example.projectcompass.domain.account;

import com.example.projectcompass.domain.project.Project;

import javax.persistence.*;

@Entity
public class AccountProject {

    @Id @GeneratedValue
    @Column(name = "account_project_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}
