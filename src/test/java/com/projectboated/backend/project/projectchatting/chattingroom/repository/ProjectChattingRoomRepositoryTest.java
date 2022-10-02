package com.projectboated.backend.project.projectchatting.chattingroom.repository;

import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.projectchatting.chattingroom.domain.ProjectChattingRoom;
import com.projectboated.backend.utils.base.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProjectChattingRoom : Persistence 단위 테스트")
class ProjectChattingRoomRepositoryTest extends RepositoryTest {

    @Test
    void findByProject_project에chattingRoom존재_return_room() {
        // Given
        Project project = insertProjectAndCaptain();
        ProjectChattingRoom projectChattingRoom = insertProjectChattingRoom(project);

        // When
        Optional<ProjectChattingRoom> result = projectChattingRoomRepository.findByProjectId(project.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(projectChattingRoom);
    }

    @Test
    void findByProject_project에chattingRoom없음_return_empty() {
        // Given
        Project project = insertProjectAndCaptain();

        // When
        Optional<ProjectChattingRoom> result = projectChattingRoomRepository.findByProjectId(project.getId());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByProject_다른project에chattingRoom존재_return_empty() {
        // Given
        Project project = insertProjectAndCaptain(USERNAME, NICKNAME);
        Project project2 = insertProjectAndCaptain(USERNAME2, NICKNAME2);
        ProjectChattingRoom projectChattingRoom2 = insertProjectChattingRoom(project2);

        // When
        Optional<ProjectChattingRoom> result = projectChattingRoomRepository.findByProjectId(project.getId());

        // Then
        assertThat(result).isEmpty();
    }

}