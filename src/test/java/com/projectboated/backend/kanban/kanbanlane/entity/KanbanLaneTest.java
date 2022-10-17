package com.projectboated.backend.kanban.kanbanlane.entity;

import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.service.dto.KanbanLaneUpdateRequest;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.uploadfile.entity.UploadFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.utils.data.BasicDataKanbanLane.*;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataProjectVideo.PROJECT_VIDEO_DESCRIPTION;
import static com.projectboated.backend.utils.data.BasicDataProjectVideo.PROJECT_VIDEO_ID;
import static com.projectboated.backend.utils.data.BasicDataUploadFile.MEDIATYPE;
import static org.assertj.core.api.Assertions.assertThat;

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
        Project project = Project.builder()
                .id(PROJECT_ID)
                .build();
        Kanban kanban = Kanban.builder()
                .id(KANBAN_ID)
                .build();

        KanbanLane dkl = KanbanLane.builder()
                .order(KANBAN_LANE_ORDER)
                .project(project)
                .kanban(kanban)
                .name(KANBAN_LANE_NAME)
                .build();

        // When
        KanbanLaneUpdateRequest request = KanbanLaneUpdateRequest.builder()
                .build();

        dkl.update(request);

        // Then
        assertThat(dkl.getName()).isEqualTo(KANBAN_LANE_NAME);
        assertThat(dkl.getProject()).isEqualTo(project);
        assertThat(dkl.getKanban()).isEqualTo(kanban);
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

    @Test
    void changeOrder_order주어짐_change성공() {
        // Given
        KanbanLane dkl = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();

        // When
        dkl.changeOrder(KANBAN_LANE_ORDER2);

        // Then
        assertThat(dkl.getOrder()).isEqualTo(KANBAN_LANE_ORDER2);
    }

    @Test
    void equals_reference가같을경우_true() {
        // Given
        KanbanLane dkl = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();

        // When
        boolean result = dkl.equals(dkl);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void equals_null인경우_false() {
        // Given
        KanbanLane dkl = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();

        // When
        boolean result = dkl.equals(null);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void equals_다른class가주어진경우_false() {
        // Given
        KanbanLane dkl = KanbanLane.builder()
                .name(KANBAN_LANE_NAME)
                .build();


        // When
        boolean result = dkl.equals("test");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void equals_id가nulL인경우_false() {
        // Given
        KanbanLane dkl1 = KanbanLane.builder()
                .id(null)
                .name(KANBAN_LANE_NAME)
                .build();
        KanbanLane dkl2 = KanbanLane.builder()
                .id(KANBAN_LANE_ID)
                .name(KANBAN_LANE_NAME)
                .build();

        // When
        boolean result = dkl1.equals(dkl2);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void equals_같은경우_true() {
        // Given
        KanbanLane dkl1 = KanbanLane.builder()
                .id(KANBAN_LANE_ID)
                .name(null)
                .build();
        KanbanLane dkl2 = KanbanLane.builder()
                .id(KANBAN_LANE_ID)
                .name(KANBAN_LANE_NAME)
                .build();

        // When
        boolean result = dkl1.equals(dkl2);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void hashCode_id가null인경우_return_0() {
        // Given
        KanbanLane dkl1 = KanbanLane.builder()
                .id(null)
                .name(null)
                .build();

        // When
        int result = dkl1.hashCode();

        // Then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void hashCode_id가null이아닌경우_return_해시값() {
        // Given
        KanbanLane dkl1 = KanbanLane.builder()
                .id(KANBAN_LANE_ID)
                .name(null)
                .build();

        // When
        // Then
        int result = dkl1.hashCode();
    }

}