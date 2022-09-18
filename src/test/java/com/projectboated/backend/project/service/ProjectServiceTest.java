package com.projectboated.backend.project.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanban.repository.KanbanRepository;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLaneType;
import com.projectboated.backend.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.project.entity.AccountProject;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.project.repository.AccountProjectRepository;
import com.projectboated.backend.project.repository.ProjectRepository;
import com.projectboated.backend.project.service.condition.GetMyProjectsCond;
import com.projectboated.backend.project.service.condition.ProjectUpdateCond;
import com.projectboated.backend.project.service.dto.MyProjectsDto;
import com.projectboated.backend.project.service.exception.ProjectNameSameInAccountException;
import com.projectboated.backend.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.projectchatting.chattingroom.repository.ChattingRoomRepository;
import com.projectboated.backend.utils.base.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID2;
import static com.projectboated.backend.utils.data.BasicDataProject.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Project : Service 단위 테스트")
class ProjectServiceTest extends ServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    AccountRepository accountRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    AccountProjectRepository accountProjectRepository;
    @Mock
    KanbanRepository kanbanRepository;
    @Mock
    KanbanLaneRepository kanbanLaneRepository;
    @Mock
    TaskRepository taskRepository;
    @Mock
    ChattingRoomRepository projectChattingRepository;

    @Test
    void findById_존재하지않는ProjectId_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.findById(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void findById_존재하는Id_return_project() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        Project byId = projectService.findById(PROJECT_ID);

        // Then
        assertThat(byId.getName()).isEqualTo(project.getName());
        assertThat(byId.getDescription()).isEqualTo(project.getDescription());
        assertThat(byId.getDeadline()).isEqualTo(project.getDeadline());
    }


    @Test
    void save_captain이가진Name으로생성_오류발생() {
        // Given
        Account captain = createAccount();
        Project project = createProject(captain);

        when(projectRepository.existsByNameAndCaptain(project.getName(), captain)).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> projectService.save(project))
                .isInstanceOf(ProjectNameSameInAccountException.class);
    }

    @Test
    void save_project저장_성공() {
        // Given
        Account captain = createAccount();
        Project project = createProject(captain);
        Kanban kanban = createKanban(project);

        when(projectRepository.existsByNameAndCaptain(project.getName(), captain)).thenReturn(false);
        when(projectRepository.save(project)).thenReturn(project);
        when(kanbanRepository.save(any())).thenReturn(kanban);

        // When
        Project saveProject = projectService.save(project);

        // Then
        assertThat(saveProject.getName()).isEqualTo(project.getName());
        assertThat(saveProject.getDescription()).isEqualTo(project.getDescription());
        assertThat(saveProject.getCaptain().getId()).isEqualTo(project.getCaptain().getId());

        verify(projectRepository).save(project);
        verify(kanbanRepository).save(any());
        verify(kanbanLaneRepository, times(KanbanLaneType.values().length)).save(any());
        verify(projectChattingRepository).save(any());
    }

    @Test
    void update_찾을수없는account_예외발생() {
        // Given
        Account captain = createAccount();
        Project project = createProject(captain);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(captain.getId(), project.getId(), new ProjectUpdateCond("newProjectName", "newProjectDescription", null)))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void update_존재하지않는projectId_오류발생() {
        // Given
        Account captain = createAccount();

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(captain.getId(), PROJECT_ID, new ProjectUpdateCond(PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE)))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void update_같은Account에같은Name존재_오류발생() {
        // Given
        Account captain = createAccount();
        Project project = createProject(captain);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectRepository.existsByNameAndCaptain(any(), any())).thenReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> projectService.update(captain.getId(), PROJECT_ID, new ProjectUpdateCond(PROJECT_NAME2, PROJECT_DESCRIPTION2, PROJECT_DEADLINE2)))
                .isInstanceOf(ProjectNameSameInAccountException.class);
    }

    @Test
    void update_이름이같은걸로update_업데이트안됨() {
        // Given
        Account captain = createAccount();
        Project project = createProject(captain);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        projectService.update(captain.getId(), PROJECT_ID, new ProjectUpdateCond(null, PROJECT_DESCRIPTION2, PROJECT_DEADLINE2));

        // Then
        assertThat(project.getName()).isEqualTo(PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(PROJECT_DESCRIPTION2);
        assertThat(project.getDeadline()).isEqualTo(PROJECT_DEADLINE2);
    }

    @Test
    void update_모든정보NULL_업데이트안됨() {
        // Given
        Account captain = createAccount();
        Project project = createProject(captain);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        projectService.update(captain.getId(), PROJECT_ID, new ProjectUpdateCond(null, null, null));

        // Then
        assertThat(project.getName()).isEqualTo(PROJECT_NAME);
        assertThat(project.getDescription()).isEqualTo(PROJECT_DESCRIPTION);
        assertThat(project.getDeadline()).isEqualTo(PROJECT_DEADLINE);
    }

    @Test
    void update_모든정보Update_업데이트성공() {
        // Given
        Account captain = createAccount();
        Project project = createProject(captain);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(projectRepository.existsByNameAndCaptain(any(), any())).thenReturn(false);

        // When
        projectService.update(captain.getId(), PROJECT_ID, new ProjectUpdateCond(PROJECT_NAME2, PROJECT_DESCRIPTION2, PROJECT_DEADLINE2));

        // Then
        assertThat(project.getName()).isEqualTo(PROJECT_NAME2);
        assertThat(project.getDescription()).isEqualTo(PROJECT_DESCRIPTION2);
        assertThat(project.getDeadline()).isEqualTo(PROJECT_DEADLINE2);
    }

    @Test
    void delete_ProjectId로찾을수없음_예외발생() {
        // Given
        Account captain = createAccount();

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.delete(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void delete_존재하는projectId와captain이요청_삭제() {
        // Given
        Account captain = createAccount();
        Project project = createProject(captain);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));

        // When
        projectService.delete(PROJECT_ID);

        // Then
        verify(taskRepository).deleteByProject(project);
        verify(kanbanLaneRepository).deleteByProject(project);
        verify(kanbanRepository).deleteByProject(project);
        verify(projectRepository).delete(project);
    }

    @Test
    void getMyProjects_찾을수없는accountId_예외발생() {
        // Given
        Account captain = createAccount();

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .captainTerm(true)
                .pageable(PageRequest.of(1, 10))
                .build();

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> projectService.getMyProjects(captain.getId(), cond))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void getMyProjects_내project조회_정상() {
        // Given
        Account captain = createAccount();

        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .captainTerm(true)
                .pageable(PageRequest.of(1, 10))
                .build();

        List<Project> projects = List.of(Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build());

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.getMyProjects(captain, cond)).thenReturn(projects);

        // When
        MyProjectsDto result = projectService.getMyProjects(captain.getId(), cond);

        // Then
        assertThat(result.getProjects()).isEqualTo(projects);
        assertThat(result.getPage()).isEqualTo(cond.getPageable().getPageNumber());
        assertThat(result.getSize()).isEqualTo(result.getProjects().size());
    }

    @Test
    void isCrew_crew가존재할때_return_true() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);

        when(accountProjectRepository.countByCrewInProject(account, project)).thenReturn(1L);

        // When
        boolean result = projectService.isCrew(project, account);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void isCrew_crew가존재하지않을때_return_false() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);

        when(accountProjectRepository.countByCrewInProject(account, project)).thenReturn(0L);

        // When
        boolean result = projectService.isCrew(project, account);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void isCaptainOrCrew_captain인경우_return_true() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);

        // When
        boolean result = projectService.isCaptainOrCrew(project, account);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void isCaptainOrCrew_crew인경우_return_true() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Account crew = createAccount(ACCOUNT_ID2);

        when(accountProjectRepository.countByCrewInProject(crew, project)).thenReturn(1L);

        // When
        boolean result = projectService.isCaptainOrCrew(project, crew);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void isCaptainOrCrew_captain과crew가아닌경우_return_false() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Account crew = createAccount(ACCOUNT_ID2);

        when(accountProjectRepository.countByCrewInProject(crew, project)).thenReturn(0L);

        // When
        boolean result = projectService.isCaptainOrCrew(project, crew);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void findByAccountId_찾을수없는Account_예외발생() {
        // Given
        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Project project2 = createProject(PROJECT_ID2, captain);
        Project project3 = createProject(PROJECT_ID3, captain);

        LocalDateTime targetDate = LocalDateTime.of(2022, 8, 1, 0, 0);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> {
            projectService.findByAccountIdAndDate(captain.getId(), targetDate);
        })
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void findByAccountId_전달시작전달끝_1개만조회됨() {
        // Given
        LocalDateTime targetDate = LocalDateTime.of(2022, 8, 1, 0, 0);

        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Project project2 = createProject(PROJECT_ID2, captain, targetDate.minusMonths(1), targetDate.minusMonths(1));
        AccountProject accountProject = createAccountProject(captain, project2);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findByCaptainAndDate(any(), any(), any())).thenReturn(new ArrayList<>(List.of(project)));
        when(accountProjectRepository.findByAccount(captain)).thenReturn(new ArrayList<>(List.of(accountProject)));

        // When
        List<Project> result = projectService.findByAccountIdAndDate(captain.getId(), targetDate);

        // Then
        assertThat(result).containsExactly(project);
    }

    @Test
    void findByAccountId_전달시작이번달끝_2개조회됨() {
        // Given
        LocalDateTime targetDate = LocalDateTime.of(2022, 8, 1, 0, 0);

        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Project project2 = createProject(PROJECT_ID2, captain, targetDate.minusMonths(1), targetDate);
        AccountProject accountProject = createAccountProject(captain, project2);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findByCaptainAndDate(any(), any(), any())).thenReturn(new ArrayList<>(List.of(project)));
        when(accountProjectRepository.findByAccount(captain)).thenReturn(new ArrayList<>(List.of(accountProject)));

        // When
        List<Project> result = projectService.findByAccountIdAndDate(captain.getId(), targetDate);

        // Then
        assertThat(result).contains(project, project2);
    }

    @Test
    void findByAccountId_전달시작다음달끝_2개조회됨() {
        // Given
        LocalDateTime targetDate = LocalDateTime.of(2022, 8, 1, 0, 0);

        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Project project2 = createProject(PROJECT_ID2, captain, targetDate.minusMonths(1), targetDate.plusMonths(1));
        AccountProject accountProject = createAccountProject(captain, project2);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findByCaptainAndDate(any(), any(), any())).thenReturn(new ArrayList<>(List.of(project)));
        when(accountProjectRepository.findByAccount(captain)).thenReturn(new ArrayList<>(List.of(accountProject)));

        // When
        List<Project> result = projectService.findByAccountIdAndDate(captain.getId(), targetDate);

        // Then
        assertThat(result).contains(project, project2);
    }

    @Test
    void findByAccountId_이번달시작이번달끝_2개조회됨() {
        // Given
        LocalDateTime targetDate = LocalDateTime.of(2022, 8, 1, 0, 0);

        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Project project2 = createProject(PROJECT_ID2, captain, targetDate, targetDate);
        AccountProject accountProject = createAccountProject(captain, project2);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findByCaptainAndDate(any(), any(), any())).thenReturn(new ArrayList<>(List.of(project)));
        when(accountProjectRepository.findByAccount(captain)).thenReturn(new ArrayList<>(List.of(accountProject)));

        // When
        List<Project> result = projectService.findByAccountIdAndDate(captain.getId(), targetDate);

        // Then
        assertThat(result).contains(project, project2);
    }

    @Test
    void findByAccountId_이번달시작다음달끝_2개조회됨() {
        // Given
        LocalDateTime targetDate = LocalDateTime.of(2022, 8, 1, 0, 0);

        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Project project2 = createProject(PROJECT_ID2, captain, targetDate, targetDate.plusMonths(1));
        AccountProject accountProject = createAccountProject(captain, project2);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findByCaptainAndDate(any(), any(), any())).thenReturn(new ArrayList<>(List.of(project)));
        when(accountProjectRepository.findByAccount(captain)).thenReturn(new ArrayList<>(List.of(accountProject)));

        // When
        List<Project> result = projectService.findByAccountIdAndDate(captain.getId(), targetDate);

        // Then
        assertThat(result).contains(project, project2);
    }

    @Test
    void findByAccountId_다음달시작다음달끝_1개조회됨() {
        // Given
        LocalDateTime targetDate = LocalDateTime.of(2022, 8, 1, 0, 0);

        Account captain = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, captain);
        Project project2 = createProject(PROJECT_ID2, captain, targetDate.plusMonths(1), targetDate.plusMonths(1));
        AccountProject accountProject = createAccountProject(captain, project2);

        when(accountRepository.findById(captain.getId())).thenReturn(Optional.of(captain));
        when(projectRepository.findByCaptainAndDate(any(), any(), any())).thenReturn(new ArrayList<>(List.of(project)));
        when(accountProjectRepository.findByAccount(captain)).thenReturn(new ArrayList<>(List.of(accountProject)));

        // When
        List<Project> result = projectService.findByAccountIdAndDate(captain.getId(), targetDate);

        // Then
        assertThat(result).contains(project);
    }

}