package com.projectboated.backend.project.project.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.entity.Project;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.projectboated.backend.account.account.entity.QAccount.account;
import static com.projectboated.backend.project.project.entity.QAccountProject.accountProject;

public class AccountProjectQueryDslRepositoryImpl implements AccountProjectQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public AccountProjectQueryDslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Account> findCrewByProject(Project project) {
        return queryFactory
                .select(account)
                .from(accountProject)
                .where(accountProject.project.eq(project))
                .fetch();
    }

}
