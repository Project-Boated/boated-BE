package my.sleepydeveloper.projectcompass.domain.invitation;

import my.sleepydeveloper.projectcompass.common.data.AccountBasicData;
import my.sleepydeveloper.projectcompass.common.data.ProjectBasicData;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.NotFoundAccountException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.invitation.exception.InviteCrewAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNotFoundException;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.criteria.CriteriaBuilder;

import java.awt.*;

import static my.sleepydeveloper.projectcompass.common.data.AccountBasicData.*;
import static my.sleepydeveloper.projectcompass.common.data.ProjectBasicData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InvitationServiceTest {

    InvitationService invitationService;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    InvitationRepository invitationRepository;

    @BeforeEach
    void init() {
        this.invitationService = new InvitationService(projectRepository, accountRepository, invitationRepository);
    }

    @Test
    @DisplayName("inviteCrew_정상")
    void inviteCrew_정상() throws Exception {
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
    @DisplayName("inviteCrew_실패_잘못된project_id")
    void inviteCrew_실패_잘못된project_id() throws Exception {
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
    @DisplayName("inviteCrew_실패_권한없음")
    void inviteCrew_실패_권한없음() throws Exception {
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
    @DisplayName("inviteCrew_실패_username으로account못찾음")
    void inviteCrew_실패_username으로account못찾음() throws Exception {
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