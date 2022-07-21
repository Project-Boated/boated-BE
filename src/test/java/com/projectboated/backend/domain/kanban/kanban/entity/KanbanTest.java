package com.projectboated.backend.domain.kanban.kanban.entity;

import com.projectboated.backend.domain.kanban.kanban.entity.exception.KanbanLaneChangeIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanban.entity.exception.KanbanLaneOriginalIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.ChangeTaskOrderRequest;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.TASK_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Kanban : Entity 단위 테스트")
class KanbanTest {

    @Test
    void 생성자_Kanban생성_return_생성된Kanban() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();

        // When
        Kanban kanban = new Kanban(project);

        // Then
        assertThat(kanban.getProject()).isEqualTo(project);
    }

    @Test
    void changeProject_새project주어짐_project변경됨() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);

        // When
        Project newProject = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME2)
                .description(PROJECT_DESCRIPTION2)
                .deadline(PROJECT_DEADLINE2)
                .build();
        kanban.changeProject(newProject);

        // Then
        assertThat(kanban.getProject()).isEqualTo(newProject);
    }

    @Test
    void addKanbanLane_새로운kanbanlane주어짐_kanbanlane추가() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);
        KanbanLane kanbanLane = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();

        // When
        kanban.addKanbanLane(kanbanLane);

        // Then
        assertThat(kanban.getLanes()).containsExactly(kanbanLane);
        assertThat(kanbanLane.getKanban()).isEqualTo(kanban);
    }

    @Test
    void changeKanbanLaneOrder_originalIndex가마이너스_예외발생() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);
        IntStream.range(0, 10)
                .forEach(i -> kanban.addKanbanLane(KanbanLane.builder()
                        .name(KANBAN_LANE_NAME + i)
                        .build()));

        // When
        // Then
        assertThatThrownBy(() -> kanban.changeKanbanLaneOrder(-1, 4))
                .isInstanceOf(KanbanLaneOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeKanbanLaneOrder_originalIndex가범위를벗어남_예외발생() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);
        IntStream.range(0, 10)
                .forEach(i -> kanban.addKanbanLane(KanbanLane.builder()
                        .name(KANBAN_LANE_NAME + i)
                        .build()));

        // When
        // Then
        assertThatThrownBy(() -> kanban.changeKanbanLaneOrder(10, 4))
                .isInstanceOf(KanbanLaneOriginalIndexOutOfBoundsException.class);
    }
    @Test
    void changeKanbanLaneOrder_changeIndex가마이너스_예외발생() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);
        IntStream.range(0, 10)
                .forEach(i -> kanban.addKanbanLane(KanbanLane.builder()
                        .name(KANBAN_LANE_NAME + i)
                        .build()));

        // When
        // Then
        assertThatThrownBy(() -> kanban.changeKanbanLaneOrder(4, -1))
                .isInstanceOf(KanbanLaneChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeKanbanLaneOrder_changeIndex가범위를벗어남_예외발생() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);
        IntStream.range(0, 10)
                .forEach(i -> kanban.addKanbanLane(KanbanLane.builder()
                        .name(KANBAN_LANE_NAME + i)
                        .build()));

        // When
        // Then
        assertThatThrownBy(() -> kanban.changeKanbanLaneOrder(8, 10))
                .isInstanceOf(KanbanLaneChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeKanbanLaneOrder_첫번째index를끝index로옮기기_정상적으로옮겨짐() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);
        IntStream.range(0, 5)
                .forEach(i -> kanban.addKanbanLane(KanbanLane.builder()
                        .name(KANBAN_LANE_NAME + i)
                        .build()));

        // When
        kanban.changeKanbanLaneOrder(0, 4);

        // Then
        assertThat(kanban.getLanes())
                .extracting("name")
                .containsExactly(KANBAN_LANE_NAME + 1,
                        KANBAN_LANE_NAME + 2,
                        KANBAN_LANE_NAME + 3,
                        KANBAN_LANE_NAME + 4,
                        KANBAN_LANE_NAME + 0);
    }

    @Test
    void changeKanbanLaneOrder_끝index를첫번째index로옮기기_정상적으로옮겨짐() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);
        IntStream.range(0, 5)
                .forEach(i -> kanban.addKanbanLane(KanbanLane.builder()
                        .name(KANBAN_LANE_NAME + i)
                        .build()));

        // When
        kanban.changeKanbanLaneOrder(4, 0);

        // Then
        assertThat(kanban.getLanes())
                .extracting("name")
                .containsExactly(KANBAN_LANE_NAME + 4,
                        KANBAN_LANE_NAME + 0,
                        KANBAN_LANE_NAME + 1,
                        KANBAN_LANE_NAME + 2,
                        KANBAN_LANE_NAME + 3);
    }

    @Test
    void changeTaskOrder_originalLaneIndex마이너스_예외발생() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);

        KanbanLane kanbanLane1 = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();
        KanbanLane kanbanLane2 = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();
        kanban.addKanbanLane(kanbanLane1);
        kanban.addKanbanLane(kanbanLane2);

        Task task1 = Task.builder()
                .name(TASK_NAME)
                .build();
        Task task2 = Task.builder()
                .name(TASK_NAME)
                .build();
        kanbanLane1.addTask(task1);
        kanbanLane1.addTask(task2);

        // When
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneIndex(-1)
                .originalTaskIndex(0)
                .changeLaneIndex(0)
                .changeTaskIndex(0)
                .build();

        // Then
        assertThatThrownBy(() -> kanban.changeTaskOrder(request))
                .isInstanceOf(KanbanLaneOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_originalLaneIndex범위벗어남_예외발생() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);

        KanbanLane kanbanLane1 = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();
        KanbanLane kanbanLane2 = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();
        kanban.addKanbanLane(kanbanLane1);
        kanban.addKanbanLane(kanbanLane2);

        Task task1 = Task.builder()
                .name(TASK_NAME)
                .build();
        Task task2 = Task.builder()
                .name(TASK_NAME)
                .build();
        kanbanLane1.addTask(task1);
        kanbanLane1.addTask(task2);

        // When
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneIndex(2)
                .originalTaskIndex(0)
                .changeLaneIndex(0)
                .changeTaskIndex(0)
                .build();

        // Then
        assertThatThrownBy(() -> kanban.changeTaskOrder(request))
                .isInstanceOf(KanbanLaneOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_changeLaneIndex마이너스범위_예외발생() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);

        KanbanLane kanbanLane1 = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();
        KanbanLane kanbanLane2 = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();
        kanban.addKanbanLane(kanbanLane1);
        kanban.addKanbanLane(kanbanLane2);

        Task task1 = Task.builder()
                .name(TASK_NAME)
                .build();
        Task task2 = Task.builder()
                .name(TASK_NAME)
                .build();
        kanbanLane1.addTask(task1);
        kanbanLane1.addTask(task2);

        // When
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneIndex(1)
                .originalTaskIndex(0)
                .changeLaneIndex(-1)
                .changeTaskIndex(0)
                .build();

        // Then
        assertThatThrownBy(() -> kanban.changeTaskOrder(request))
                .isInstanceOf(KanbanLaneChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_changeLaneIndex범위벗어남_예외발생() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);

        KanbanLane kanbanLane1 = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();
        KanbanLane kanbanLane2 = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();
        kanban.addKanbanLane(kanbanLane1);
        kanban.addKanbanLane(kanbanLane2);

        Task task1 = Task.builder()
                .name(TASK_NAME)
                .build();
        Task task2 = Task.builder()
                .name(TASK_NAME)
                .build();
        kanbanLane1.addTask(task1);
        kanbanLane1.addTask(task2);

        // When
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneIndex(1)
                .originalTaskIndex(0)
                .changeLaneIndex(2)
                .changeTaskIndex(0)
                .build();

        // Then
        assertThatThrownBy(() -> kanban.changeTaskOrder(request))
                .isInstanceOf(KanbanLaneChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_정상request_change성공() {
        // Given
        Project project = Project.builder()
                .captain(ACCOUNT)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
        Kanban kanban = new Kanban(project);

        KanbanLane kanbanLane1 = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();
        KanbanLane kanbanLane2 = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();
        kanban.addKanbanLane(kanbanLane1);
        kanban.addKanbanLane(kanbanLane2);

        Task task1 = Task.builder()
                .name(TASK_NAME + 1)
                .build();
        Task task2 = Task.builder()
                .name(TASK_NAME + 2)
                .build();
        kanbanLane1.addTask(task1);
        kanbanLane1.addTask(task2);
        Task task3 = Task.builder()
                .name(TASK_NAME + 3)
                .build();
        Task task4 = Task.builder()
                .name(TASK_NAME + 4)
                .build();
        kanbanLane2.addTask(task3);
        kanbanLane2.addTask(task4);

        // When
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .originalLaneIndex(0)
                .originalTaskIndex(0)
                .changeLaneIndex(1)
                .changeTaskIndex(1)
                .build();

        kanban.changeTaskOrder(request);

        // Then
        assertThat(kanbanLane1.getTasks()).extracting("name")
                .containsExactly(TASK_NAME+2);
        assertThat(kanbanLane2.getTasks()).extracting("name")
                .containsExactly(TASK_NAME+3, TASK_NAME+1, TASK_NAME+4);
    }

}