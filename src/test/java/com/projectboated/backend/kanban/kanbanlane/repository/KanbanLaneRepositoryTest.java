package com.projectboated.backend.kanban.kanbanlane.repository;

import com.projectboated.backend.utils.base.RepositoryTest;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.NICKNAME;
import static com.projectboated.backend.utils.data.BasicDataAccount.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

class KanbanLaneRepositoryTest extends RepositoryTest {
	
	@Test
	void countByKanban_kanban에속한lane4개존재_return_4() {
		// Given
		Project project = insertProjectAndCaptain();
		Kanban kanban = insertKanban(project);
		insertKanbanLanes(project, kanban, 4);
		
		// When
		Long result = kanbanLaneRepository.countByKanban(kanban);
		
		// Then
		assertThat(result).isEqualTo(4);
	}
	
	@Test
	void findByProjectAndKanbanLaneId_project에속한lane5개존재_1개조회() {
		// Given
		Project project = insertProjectAndCaptain();
		Kanban kanban = insertKanban(project);
		List<KanbanLane> kanbanLanes = insertKanbanLanes(project, kanban, 5);
		flushAndClear();
		
		// When
		KanbanLane target = kanbanLanes.get(2);
		Optional<KanbanLane> result = kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), target.getId());
		
		// Then
		assertThat(result).isPresent();
		assertThat(result.get().getName()).isEqualTo(target.getName());
	}
	
	@Test
	void findByProjectAndKanbanLaneId_project에속한lane존재하지않음_return_empty() {
		// Given
		Project project = insertProjectAndCaptain();
		Kanban kanban = insertKanban(project);
		
		// When
		Optional<KanbanLane> result = kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), 123L);
		
		// Then
		assertThat(result).isEmpty();
	}

	@Test
	void findByProject_project에속한lane4개존재_4개조회() {
	    // Given
		Project project = insertProjectAndCaptain(USERNAME, NICKNAME);
		Kanban kanban = insertKanban(project);
		insertKanbanLanes(project, kanban, 4);

		// When
		List<KanbanLane> result = kanbanLaneRepository.findByProject(project);

		// Then
		assertThat(result).hasSize(4);
	}
	
}