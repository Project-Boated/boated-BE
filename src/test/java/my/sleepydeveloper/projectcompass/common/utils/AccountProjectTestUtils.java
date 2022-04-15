package my.sleepydeveloper.projectcompass.common.utils;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.accountproject.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.accountproject.repository.AccountProjectRepository;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;

@Service
@Transactional
public class AccountProjectTestUtils {

    private final AccountProjectRepository accountProjectRepository;

    public AccountProjectTestUtils(AccountProjectRepository accountProjectRepository) {
        this.accountProjectRepository = accountProjectRepository;
    }

    public AccountProject createAccountRepository(Account account, Project project) {
        return accountProjectRepository.save(new AccountProject(account, project));
    }
}
