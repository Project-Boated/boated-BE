package my.sleepydeveloper.projectcompass.domain.invitation;

import my.sleepydeveloper.projectcompass.common.basetest.UnitTest;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.NotFoundAccountException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.invitation.entity.Invitation;
import my.sleepydeveloper.projectcompass.domain.invitation.exception.InviteCrewAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNotFoundException;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.projectDescription;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.projectName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.context.annotation.ComponentScan.Filter;

@DataJpaTest(
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = InvitationService.class)
)
class InvitationServiceTest extends UnitTest {

    @Autowired
    InvitationService invitationService;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    InvitationRepository invitationRepository;

    @Test
    void inviteCrew_초대1개생성_정상() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(username, password, nickname, "ROLE_USER"));
        Project project = projectRepository.save(new Project(projectName, projectDescription, captain));
        String crewNickname = "crew";
        String crewUsername = "crew";
        Account crew = accountRepository.save(new Account(crewUsername, password, crewNickname, "ROLE_USER"));

        // When
        Invitation invitation = invitationService.inviteCrew(captain, crewUsername, project.getId());

        // Then
        assertThat(invitation.getId()).isNotNull();
        assertThat(invitation.getProject()).isEqualTo(project);
        assertThat(invitation.getAccount()).isEqualTo(crew);
    }

    @Test
    void inviteCrew_존재하지않는ProjectId_오류발생() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(username, password, nickname, "ROLE_USER"));
        Project project = projectRepository.save(new Project(projectName, projectDescription, captain));
        String crewNickname = "crew";
        String crewUsername = "crew";
        Account crew = accountRepository.save(new Account(crewUsername, password, crewNickname, "ROLE_USER"));

        // When
        // Then
        assertThatThrownBy(() -> invitationService.inviteCrew(captain, crewUsername, 91239L))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void inviteCrew_captain이아닌account가초대생성_오류발생() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(username, password, nickname, "ROLE_USER"));
        Project project = projectRepository.save(new Project(projectName, projectDescription, captain));
        String crewNickname = "crew";
        String crewUsername = "crew";
        Account crew = accountRepository.save(new Account(crewUsername, password, crewNickname, "ROLE_USER"));

        // When
        // Then
        assertThatThrownBy(() -> invitationService.inviteCrew(crew, crewUsername, project.getId()))
                .isInstanceOf(InviteCrewAccessDenied.class);
    }

    @Test
    void inviteCrew_없는username으로초대_오류발생() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(username, password, nickname, "ROLE_USER"));
        Project project = projectRepository.save(new Project(projectName, projectDescription, captain));
        String crewNickname = "crew";
        String crewUsername = "crew";
        Account crew = accountRepository.save(new Account(crewUsername, password, crewNickname, "ROLE_USER"));

        // When
        // Then
        assertThatThrownBy(() -> invitationService.inviteCrew(captain, "failusername", project.getId()))
                .isInstanceOf(NotFoundAccountException.class);
    }
}