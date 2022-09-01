package com.projectboated.backend.domain.projectchatting.projectchattingroom.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectchatting.projectchattingroom.domain.ProjectChattingRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProjectChattingRoom : Persistence 단위 테스트")
class ProjectChattingRoomRepositoryTest extends RepositoryTest {

    @Test
    void findByProject_project에chatting방이존재하지않는경우_return_empty(){
        // Given
        Project project = insertProjectAndCaptain();

        // When
        Optional<ProjectChattingRoom> result = projectChattingRoomRepository.findByProject(project);

        // Then
        assertThat(result).isEmpty();
    }
    
    @Test
    void findByProject_project에chatting방이존재하는경우_return_ProjectChattingRoom(){
        // Given
        Project project = insertProjectAndCaptain();
        insertProjectChattingRoom(project);

        // When
        Optional<ProjectChattingRoom> result = projectChattingRoomRepository.findByProject(project);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getProject()).isEqualTo(project);
    }

}