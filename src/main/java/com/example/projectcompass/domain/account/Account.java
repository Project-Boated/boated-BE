package com.example.projectcompass.domain.account;

import com.example.projectcompass.domain.common.Entity.BaseTimeEntity;

import javax.persistence.*;

@Entity
public class Account extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private String email;

    private String password;

    private String nickname;
}
