package my.sleepydeveloper.projectcompass.domain.accountproject.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import my.sleepydeveloper.projectcompass.domain.accountproject.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.QAccount;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.entity.QProject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static my.sleepydeveloper.projectcompass.domain.accountproject.entity.QAccountProject.accountProject;
import static my.sleepydeveloper.projectcompass.domain.account.entity.QAccount.account;
import static my.sleepydeveloper.projectcompass.domain.project.entity.QProject.*;

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
}
