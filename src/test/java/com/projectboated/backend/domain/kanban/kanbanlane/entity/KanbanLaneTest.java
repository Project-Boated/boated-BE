package com.projectboated.backend.domain.kanban.kanbanlane.entity;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.exception.TaskChangeIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.exception.TaskOriginalIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.KanbanLaneUpdateRequest;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataKanbanLane.*;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("KanbanLane : Entity 단위 테스트")
class KanbanLaneTest {

    @Test
    void 생성자_KanbanLane생성_return_생성된KanbanLane() {
        // Given
        Project project = Project.builder().build();
        Kanban kanban = Kanban.builder().project(project).build();

        // When
        KanbanLane kanbanLane = new KanbanLane(KANBAN_LANE_ID, KANBAN_LANE_NAME, kanban);

        // Then
        assertThat(kanbanLane.getKanban()).isEqualTo(kanban);
    }


    @Test
    void changeKanban_바꿀Kanban주어짐_changeKanban() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();

        KanbanLane dkl = KanbanLane.builder()
                .kanban(kanban)
                .build();

        Project project2 = Project.builder()
                .build();
        Kanban kanban2 = Kanban.builder()
                .project(project2)
                .build();

        // When
        dkl.changeKanban(kanban2);

        // Then
        assertThat(dkl.getKanban()).isEqualTo(kanban2);
    }

    @Test
    void addTask_추가할task주어짐_task추가() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        KanbanLane dkl = KanbanLane.builder()
                .kanban(kanban)
                .build();

        // When
        Task task = Task.builder()
                .name(TASK_NAME)
                .description(TASK_DESCRIPTION)
                .deadline(TASK_DEADLINE)
                .build();

        dkl.addTask(task);

        // Then
        assertThat(task.getKanbanLane()).isEqualTo(dkl);
        assertThat(dkl.getTasks()).containsExactly(task);
    }

    @Test
    void changeTaskOrder_originalIndex가마이너스_예외발생() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        KanbanLane dkl = KanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 10)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));

        // When
        // Then
        assertThatThrownBy(() -> dkl.changeTaskOrder(-1, 4))
                .isInstanceOf(TaskOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_originalIndex가범위를벗어남_예외발생() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        KanbanLane dkl = KanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 10)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));

        // When
        // Then
        assertThatThrownBy(() -> dkl.changeTaskOrder(10, 4))
                .isInstanceOf(TaskOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_changeIndex가마이너스_예외발생() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        KanbanLane dkl = KanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 10)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));
        // When
        // Then
        assertThatThrownBy(() -> dkl.changeTaskOrder(4, -1))
                .isInstanceOf(TaskChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_changeIndex가범위를벗어남_예외발생() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        KanbanLane dkl = KanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 10)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));

        // When
        // Then
        assertThatThrownBy(() -> dkl.changeTaskOrder(8, 10))
                .isInstanceOf(TaskChangeIndexOutOfBoundsException.class);
    }

    @Test
    void changeTaskOrder_첫번째index를끝index로옮기기_정상적으로옮겨짐() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        KanbanLane dkl = KanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 5)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));

        // When
        dkl.changeTaskOrder(0, 4);

        // Then
        assertThat(dkl.getTasks())
                .extracting("name")
                .containsExactly(TASK_NAME + 1,
                        TASK_NAME + 2,
                        TASK_NAME + 3,
                        TASK_NAME + 4,
                        TASK_NAME + 0);
    }

    @Test
    void changeTaskOrder_끝index를첫번째index로옮기기_정상적으로옮겨짐() {
        // Given
        Project project = Project.builder()
                .build();
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();
        KanbanLane dkl = KanbanLane.builder()
                .kanban(kanban)
                .build();
        IntStream.range(0, 5)
                .forEach(i -> dkl.addTask(Task.builder()
                        .name(TASK_NAME + i)
                        .build()));

        // When
        dkl.changeTaskOrder(4, 0);

        // Then
        assertThat(dkl.getTasks())
                .extracting("name")
                .containsExactly(TASK_NAME + 4,
                        TASK_NAME + 0,
                        TASK_NAME + 1,
                        TASK_NAME + 2,
                        TASK_NAME + 3);
    }
    
    @Test
    void addTask_첫번째에task추가_정상() {
        // Given
        KanbanLane dkl = KanbanLane
                .builder()
                .build();

        // When
        dkl.addTask(0, new Task(TASK_ID, TASK_NAME, TASK_DESCRIPTION, null));
        
        // Then
        assertThat(dkl.getTasks())
                .extracting("name")
                .containsExactly(TASK_NAME);
    }

    @Test
    void addTask_마지막에task추가_정상() {
        // Given
        KanbanLane dkl = KanbanLane
                .builder()
                .build();

        dkl.addTask(new Task(TASK_ID, TASK_NAME+1, TASK_DESCRIPTION, null));
        dkl.addTask(new Task(TASK_ID2, TASK_NAME+2, TASK_DESCRIPTION, null));

        // When
        dkl.addTask(2, new Task(TASK_ID, TASK_NAME+3, TASK_DESCRIPTION, null));

        // Then
        assertThat(dkl.getTasks())
                .extracting("name")
                .containsExactly(TASK_NAME+1, TASK_NAME+2, TASK_NAME+3);
    }

    @Test
    void addTask_중간에task추가_정상() {
        // Given
        KanbanLane dkl = KanbanLane
                .builder()
                .build();

        dkl.addTask(new Task(TASK_ID, TASK_NAME+1, TASK_DESCRIPTION, null));
        dkl.addTask(new Task(TASK_ID, TASK_NAME+2, TASK_DESCRIPTION, null));
        dkl.addTask(new Task(TASK_ID, TASK_NAME+3, TASK_DESCRIPTION, null));

        // When
        dkl.addTask(2, new Task(TASK_ID, TASK_NAME+4, TASK_DESCRIPTION, null));

        // Then
        assertThat(dkl.getTasks())
                .extracting("name")
                .containsExactly(TASK_NAME+1, TASK_NAME+2, TASK_NAME+4, TASK_NAME+3);
    }

    @Test
    void addTask_마이너스인덱스_예외발생() {
        // Given
        KanbanLane dkl = KanbanLane
                .builder()
                .build();

        // When
        // Then
        assertThatThrownBy(() -> dkl.addTask(-1, new Task(TASK_ID, TASK_NAME+4, TASK_DESCRIPTION, null)))
                .isInstanceOf(TaskChangeIndexOutOfBoundsException.class);
    }

    @Test
    void addTask_인덱스벗어남_예외발생() {
        // Given
        KanbanLane dkl = KanbanLane
                .builder()
                .build();

        dkl.addTask(new Task(TASK_ID, TASK_NAME+1, TASK_DESCRIPTION, null));
        dkl.addTask(new Task(TASK_ID, TASK_NAME+2, TASK_DESCRIPTION, null));
        dkl.addTask(new Task(TASK_ID, TASK_NAME+3, TASK_DESCRIPTION, null));

        // When
        // Then
        assertThatThrownBy(() -> dkl.addTask(4, new Task(TASK_ID, TASK_NAME+4, TASK_DESCRIPTION, null)))
                .isInstanceOf(TaskChangeIndexOutOfBoundsException.class);
    }

    @Test
    void removeTask_task삭제_정상() {
        // Given
        KanbanLane dkl = KanbanLane
                .builder()
                .build();

        dkl.addTask(new Task(TASK_ID, TASK_NAME+1, TASK_DESCRIPTION, null));
        dkl.addTask(new Task(TASK_ID, TASK_NAME+2, TASK_DESCRIPTION, null));
        dkl.addTask(new Task(TASK_ID, TASK_NAME+3, TASK_DESCRIPTION, null));

        // When
        dkl.removeTask(1);

        // Then
        assertThat(dkl.getTasks())
                .extracting("name")
                .containsExactly(TASK_NAME+1, TASK_NAME+3);
    }

    @Test
    void removeTask_마이너스인덱스_예외발생() {
        // Given
        KanbanLane dkl = KanbanLane
                .builder()
                .build();

        // When
        // Then
        assertThatThrownBy(() -> dkl.removeTask(-1))
                .isInstanceOf(TaskOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void removeTask_인덱스벗어남_예외발생() {
        // Given
        KanbanLane dkl = KanbanLane
                .builder()
                .build();

        dkl.addTask(new Task(TASK_ID, TASK_NAME+1, TASK_DESCRIPTION, null));
        dkl.addTask(new Task(TASK_ID, TASK_NAME+2, TASK_DESCRIPTION, null));
        dkl.addTask(new Task(TASK_ID, TASK_NAME+3, TASK_DESCRIPTION, null));

        // When
        // Then
        assertThatThrownBy(() -> dkl.removeTask(3))
                .isInstanceOf(TaskOriginalIndexOutOfBoundsException.class);
    }

    @Test
    void update_name이null인경우_업데이트안함() {
        // Given
        KanbanLane dkl = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();

        // When
        KanbanLaneUpdateRequest request = KanbanLaneUpdateRequest.builder()
                .name(null)
                .build();

        dkl.update(request);

        // Then
        assertThat(dkl.getName()).isEqualTo(KANBAN_LANE_NAME);
    }

    @Test
    void update_이름만바꿈_업데이트성공() {
        // Given
        KanbanLane dkl = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();

        // When
        KanbanLaneUpdateRequest request = KanbanLaneUpdateRequest.builder()
                .name(KANBAN_LANE_NAME2)
                .build();

        dkl.update(request);

        // Then
        assertThat(dkl.getName()).isEqualTo(KANBAN_LANE_NAME2);
    }

}