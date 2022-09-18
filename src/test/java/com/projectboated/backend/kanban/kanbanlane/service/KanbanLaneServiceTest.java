package com.projectboated.backend.kanban.kanbanlane.service;

import com.projectboated.backend.kanban.kanbanlane.service.KanbanLaneService;
import com.projectboated.backend.utils.base.ServiceTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanban.entity.exception.KanbanLaneChangeIndexOutOfBoundsException;
import com.projectboated.backend.kanban.kanban.entity.exception.KanbanLaneOriginalIndexOutOfBoundsException;
import com.projectboated.backend.kanban.kanban.repository.KanbanRepository;
import com.projectboated.backend.kanban.kanban.service.exception.KanbanNotFoundException;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.kanban.kanbanlane.service.dto.KanbanLaneUpdateRequest;
import com.projectboated.backend.kanban.kanbanlane.service.exception.KanbanLaneAlreadyExists5;
import com.projectboated.backend.kanban.kanbanlane.service.exception.KanbanLaneExists1Exception;
import com.projectboated.backend.kanban.kanbanlane.service.exception.KanbanLaneExistsTaskException;
import com.projectboated.backend.kanban.kanbanlane.service.exception.KanbanLaneNotFoundException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.utils.data.BasicDataKanban.KANBAN_ID2;
import static com.projectboated.backend.utils.data.BasicDataKanbanLane.*;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("KanbanLane : Service 단위 테스트")
class KanbanLaneServiceTest extends ServiceTest {

    @InjectMocks
    KanbanLaneService kanbanLaneService;

    @Mock
    ProjectRepository projectRepository;
    @Mock
    KanbanLaneRepository kanbanLaneRepository;
    @Mock
    TaskRepository taskRepository;
    @Mock
    KanbanRepository kanbanRepository;

    @Test
    void findByProjectId_존재하지않는프로젝트_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.findByProjectId(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void findByProjectId_4개존재_4개조회() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 4);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);

        // When
        List<KanbanLane> result = kanbanLaneService.findByProjectId(PROJECT_ID);

        // Then
        assertThat(result).isEqualTo(kanbanLanes);

        verify(projectRepository).findById(PROJECT_ID);
        verify(kanbanLaneRepository).findByProject(project);
    }

    @Test
    void createNewLine_projectId찾을수없음_에외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.createNewLine(PROJECT_ID, KANBAN_LANE_NAME))
                .isInstanceOf(ProjectNotFoundException.class);

        verify(projectRepository).findById(PROJECT_ID);
    }

    @Test
    void createNewLine_kanban을찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanRepository.findByProject(project)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.createNewLine(PROJECT_ID, KANBAN_LANE_NAME))
                .isInstanceOf(KanbanNotFoundException.class);
    }

    @Test
    void createNewLine_이미lane이5개존재_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 5);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanRepository.findByProject(project)).thenReturn(Optional.of(kanban));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.createNewLine(PROJECT_ID, KANBAN_LANE_NAME))
                .isInstanceOf(KanbanLaneAlreadyExists5.class);
    }

    @Test
    void createNewLine_정상요청_정상() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 2);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanRepository.findByProject(project)).thenReturn(Optional.of(kanban));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);

        // When
        kanbanLaneService.createNewLine(PROJECT_ID, KANBAN_LANE_NAME);

        // Then
        verify(kanbanLaneRepository).save(any());
    }

    @Test
    void updateKanbanLane_kanbanLane을찾을수없음_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        KanbanLane kanbanLane = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 1).get(0);

        when(kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), kanbanLane.getId())).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.updateKanbanLane(project.getId(), kanbanLane.getId(), null))
                .isInstanceOf(KanbanLaneNotFoundException.class);
    }

    @Test
    void updateKanbanLane_정상적인입력_정상update() {
        // Given
        Account account = createAccount();
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 1).get(0);

        Project project2 = createProject(PROJECT_ID2, account);
        Kanban kanban2 = createKanban(KANBAN_ID2, project);

        KanbanLaneUpdateRequest request = KanbanLaneUpdateRequest.builder()
                .name(KANBAN_LANE_NAME2)
                .order(KANBAN_LANE_ORDER2)
                .project(project2)
                .kanban(kanban2)
                .build();

        when(kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), kanbanLane.getId())).thenReturn(Optional.of(kanbanLane));

        // When
        kanbanLaneService.updateKanbanLane(project.getId(), kanbanLane.getId(), request);

        // Then
        assertThat(kanbanLane.getName()).isEqualTo(KANBAN_LANE_NAME2);
        assertThat(kanbanLane.getOrder()).isEqualTo(KANBAN_LANE_ORDER2);
        assertThat(kanbanLane.getProject()).isEqualTo(project2);
        assertThat(kanbanLane.getKanban()).isEqualTo(kanban2);
    }

    @Test
    void deleteKanbanLane_프로젝트를찾을수없음_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(PROJECT_ID, KANBAN_LANE_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void deleteCustomLane_lane이1개밖에없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 1);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(List.of(kanbanLanes.get(0)));

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(PROJECT_ID, KANBAN_LANE_ID))
                .isInstanceOf(KanbanLaneExists1Exception.class);
    }

    @Test
    void deleteCustomLane_lane에task가있는경우_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 2);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);
        when(taskRepository.countByKanbanLaneId(KANBAN_LANE_ID)).thenReturn(4L);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(PROJECT_ID, KANBAN_LANE_ID))
                .isInstanceOf(KanbanLaneExistsTaskException.class);
    }

    @Test
    void deleteCustomLane_kanbanLane을찾을수없음_예외발생() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 2);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);
        when(taskRepository.countByKanbanLaneId(KANBAN_LANE_ID + 10)).thenReturn(0L);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.deleteKanbanLane(PROJECT_ID, KANBAN_LANE_ID + 10))
                .isInstanceOf(KanbanLaneNotFoundException.class);
    }


    @Test
    void deleteCustomLane_정상입력_정상delete() {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 2);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);
        when(taskRepository.countByKanbanLaneId(KANBAN_LANE_ID+1)).thenReturn(0L);

        // When
        kanbanLaneService.deleteKanbanLane(PROJECT_ID, KANBAN_LANE_ID+1);

        // Then
        assertThat(kanbanLanes).extracting("id", "order")
                .containsExactly(tuple(KANBAN_LANE_ID, 0));

        verify(kanbanLaneRepository).delete(any());
    }


    @Test
    void changeKanbanLaneOrder_projectId로찾을수없음_예외발생() {
        // Given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.changeKanbanLaneOrder(PROJECT_ID, 0, 0))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void changeKanbanLaneOrder_originalLaneIndex가마이너스인경우_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 4);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.changeKanbanLaneOrder(PROJECT_ID, -1, 0))
                .isInstanceOf(KanbanLaneOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeKanbanLaneOrder_originalLaneIndex가size를넘어설경우_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 4);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.changeKanbanLaneOrder(PROJECT_ID, 4, 0))
                .isInstanceOf(KanbanLaneOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeKanbanLaneOrder_changeIndex가마이너스일경우_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 4);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.changeKanbanLaneOrder(PROJECT_ID, 1, -1))
                .isInstanceOf(KanbanLaneChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeKanbanLaneOrder_changeIndex가범위를벗어날경우_예외발생() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 4);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);

        // When
        // Then
        assertThatThrownBy(() -> kanbanLaneService.changeKanbanLaneOrder(PROJECT_ID, 1, 4))
                .isInstanceOf(KanbanLaneChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeKanbanLaneOrder_정상적인경우_정상change() {
        // Given
        Account account = createAccount();
        Project project = createProject(account);
        Kanban kanban = createKanban(project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 4);

        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(kanbanLaneRepository.findByProject(project)).thenReturn(kanbanLanes);

        // When
        kanbanLaneService.changeKanbanLaneOrder(PROJECT_ID, 0, 3);

        // Then
        assertThat(kanbanLanes).extracting("id")
                .containsExactly(KANBAN_LANE_ID+1, KANBAN_LANE_ID+2, KANBAN_LANE_ID+3, KANBAN_LANE_ID);
    }

}