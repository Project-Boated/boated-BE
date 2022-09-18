package com.projectboated.backend.project.projectchatting.chattingroom.domain;

import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.projectchatting.chattingroom.domain.ProjectChattingRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataProjectChattingRoom.PROJECT_CHATTING_ROOM_ID;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProjectChattingRoom : Entity 단위 테스트")
class ProjectChattingRoomTest {

    @Test
    void 생성자_정상적인param_생성성공() {
        // Given
        Project project = Project.builder()
                .build();

        // When
        ProjectChattingRoom projectChattingRoom = new ProjectChattingRoom(PROJECT_CHATTING_ROOM_ID, project);

        // Then
        assertThat(projectChattingRoom.getId()).isEqualTo(PROJECT_CHATTING_ROOM_ID);
        assertThat(projectChattingRoom.getProject()).isEqualTo(project);
    }

}