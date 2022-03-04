package my.sleepydeveloper.projectcompass.domain.account.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.account.entity.QAccount;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.entity.QProject;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static my.sleepydeveloper.projectcompass.domain.account.entity.QAccount.*;
import static my.sleepydeveloper.projectcompass.domain.account.entity.QAccountProject.*;

@Repository
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
}
