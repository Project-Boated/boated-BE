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
        KanbanLane kanbanLane = new KanbanLane(KANBAN_LANE_ID, KANBAN_LANE_NAME, 0, project, kanban);

        // Then
        assertThat(kanbanLane.getKanban()).isEqualTo(kanban);
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