package com.example.projectcompass.domain.project;

import com.example.projectcompass.domain.account.Account;
import com.example.projectcompass.domain.common.Entity.BaseTimeEntity;

import javax.persistence.*;

@Entity
public class Project extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    private String name;

    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id_captain")
    private Account captain;
}
