package my.sleepydeveloper.projectcompass.domain.invitation;

import my.sleepydeveloper.projectcompass.common.basetest.BaseTest;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountNotFoundException;
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

import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.PROJECT_DESCRIPTION;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.PROJECT_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.context.annotation.ComponentScan.Filter;

@DataJpaTest(
    includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = InvitationService.class)
)
class InvitationServiceTest extends BaseTest {

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
        Account captain = accountRepository.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));
        Project project = projectRepository.save(new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain));
        String crewNickname = "crew";
        String crewUsername = "crew";
        Account crew = accountRepository.save(new Account(crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL, ROLES));

        // When
        Invitation invitation = invitationService.inviteCrew(project.getId(), crewUsername, captain);

        // Then
        assertThat(invitation.getId()).isNotNull();
        assertThat(invitation.getProject()).isEqualTo(project);
        assertThat(invitation.getAccount()).isEqualTo(crew);
    }

    @Test
    void inviteCrew_존재하지않는ProjectId_오류발생() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));
        Project project = projectRepository.save(new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain));
        String crewNickname = "crew";
        String crewUsername = "crew";
        Account crew = accountRepository.save(new Account(crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL, ROLES));

        // When
        // Then
        assertThatThrownBy(() -> invitationService.inviteCrew(91239L, crewUsername, captain))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void inviteCrew_captain이아닌account가초대생성_오류발생() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));
        Project project = projectRepository.save(new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain));
        String crewNickname = "crew";
        String crewUsername = "crew";
        Account crew = accountRepository.save(new Account(crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL, ROLES));

        // When
        // Then
        assertThatThrownBy(() -> invitationService.inviteCrew(project.getId(), crewUsername, crew))
                .isInstanceOf(InviteCrewAccessDenied.class);
    }

    @Test
    void inviteCrew_없는username으로초대_오류발생() throws Exception {
        // Given
        Account captain = accountRepository.save(new Account(USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL, ROLES));
        Project project = projectRepository.save(new Project(PROJECT_NAME, PROJECT_DESCRIPTION, captain));
        String crewNickname = "crew";
        String crewUsername = "crew";
        Account crew = accountRepository.save(new Account(crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL, ROLES));

        // When
        // Then
        assertThatThrownBy(() -> invitationService.inviteCrew(project.getId(), "failusername", captain))
                .isInstanceOf(AccountNotFoundException.class);
    }
}