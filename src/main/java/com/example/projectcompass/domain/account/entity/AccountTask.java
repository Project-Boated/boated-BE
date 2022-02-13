package com.example.projectcompass.domain.account.entity;

import com.example.projectcompass.domain.common.Entity.BaseTimeEntity;
import com.example.projectcompass.domain.task.Task;

import javax.persistence.*;

@Entity
public class AccountTask extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_task_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;
}
