package com.projectboated.backend.domain.kanban.kanbanlane.entity;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.project.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DefaultKanbanLane : Entity 단위 테스트")
class DefaultKanbanLaneTest {

    @Test
    void 생성자_DefaultKanbanLane생성_return_생성된DefaultKanbanLane() {
        // Given
        Project project = Project.builder().build();
        Kanban kanban = Kanban.builder().project(project).build();

        // When
        KanbanLane kanbanLane = new DefaultKanbanLane(KANBAN_LANE_NAME, kanban);

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

        DefaultKanbanLane dkl = DefaultKanbanLane.builder()
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

}