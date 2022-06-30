package com.projectboated.backend.domain.accountproject.repository;

import com.projectboated.backend.domain.account.entity.Account;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.projectboated.backend.domain.accountproject.entity.AccountProject;
import com.projectboated.backend.domain.account.entity.QAccount;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.entity.QProject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.projectboated.backend.domain.accountproject.entity.QAccountProject.accountProject;
import static com.projectboated.backend.domain.account.entity.QAccount.account;
import static com.projectboated.backend.domain.project.entity.QProject.*;

@Repository
@Transactional
public class AccountProjectRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public AccountProjectRepository(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    public AccountProject save(AccountProject accountProject) {
        em.persist(accountProject);
        return accountProject;
    }

    public List<Account> findCrewsFromProject(Project project) {
        return queryFactory
                .select(account)
                .from(accountProject)
                .where(accountProject.project.eq(project))
                .fetch();
    }

    public long delete(Project project, Account account) {
        return queryFactory
                .delete(accountProject)
                .where(QProject.project.eq(project).and(QAccount.account.eq(account)))
                .execute();
    }

    public boolean existsAccountInProject(Account crew, Project project) {
        return queryFactory
                .selectFrom(accountProject)
                .where(accountProject.project.eq(project).and(accountProject.account.eq(crew))) != null;
    }

    public List<Project> findProjectFromCrew(Account account) {
        return queryFactory
                .select(project)
                .from(accountProject)
                .where(accountProject.account.eq(account))
                .fetch();
    }

    public List<Project> findProjectFromCrewNotTerminated(Account account) {
        return queryFactory
                .select(project)
                .from(accountProject)
                .where(accountProject.account.eq(account).and(accountProject.project.isTerminated.eq(false)))
                .fetch();
    }

    public List<Project> findProjectFromCrewTerminated(Account account) {
        return queryFactory
                .select(project)
                .from(accountProject)
                .where(accountProject.account.eq(account).and(accountProject.project.isTerminated.eq(true)))
                .fetch();
    }

    public Long countByCrewInProject(Long crewId, Long projectId) {
        return (Long) em.createQuery("select count(ap) from AccountProject ap " +
                "where ap.account.id=:crewId and ap.project.id=:projectId")
                .setParameter("crewId", crewId)
                .setParameter("projectId", projectId)
                .getSingleResult();
    }
}
