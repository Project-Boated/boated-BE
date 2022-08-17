package com.projectboated.backend.domain.kanban.kanban.entity;

import com.projectboated.backend.domain.kanban.kanban.entity.exception.KanbanLaneChangeIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanban.entity.exception.KanbanLaneOriginalIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.ChangeTaskOrderRequest;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.KanbanLaneUpdateRequest;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneAlreadyExists5;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME2;
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
        Kanban kanban = new Kanban(KANBAN_ID, project);

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
        Kanban kanban = Kanban.builder()
                .project(project)
                .build();

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

}