package my.sleepydeveloper.projectcompass.domain.invitation;

import my.sleepydeveloper.projectcompass.common.data.AccountBasicData;
import my.sleepydeveloper.projectcompass.common.data.ProjectBasicData;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.ProjectTestUtils;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.parameters.P;

import static my.sleepydeveloper.projectcompass.common.data.AccountBasicData.*;
import static my.sleepydeveloper.projectcompass.common.data.AccountBasicData.username;
import static my.sleepydeveloper.projectcompass.common.data.ProjectBasicData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InvitationRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    InvitationRepository invitationRepository;

    @Test
    @DisplayName("save_작동확인")
    void save_작동확인() throws Exception {
        // Given
        String captainUsername = "captain";
        String captainNickname = "captain";

        Account captain = accountRepository.save(new Account(captainUsername, password, captainNickname, "ROLE_USER"));
        Account account = accountRepository.save(new Account(username, password, nickname, "ROLE_USER"));
        Project project = projectRepository.save(new Project(projectName, projectDescription, captain));

        // When
        Invitation invitation = invitationRepository.save(new Invitation(account, project));

        // Then
        assertThat(invitation.getAccount()).isEqualTo(account);
        assertThat(invitation.getProject()).isEqualTo(project);
    }
}