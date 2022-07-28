package com.projectboated.backend.domain.kanban.kanbanlane.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataKanbanLane.*;
import static org.assertj.core.api.Assertions.assertThat;

class KanbanLaneRepositoryTest extends RepositoryTest {
	
	@Test
	void findByKanbanAndName_kanban에속한name1개존재_1개조회() {
		// Given
		Project project = insertDefaultProjectAndDefaultCaptain();
		Kanban kanban = insertKanban(project);
		insertKanbanLane(kanban);
		
		// When
		Optional<KanbanLane> result = kanbanLaneRepository.findByKanbanAndName(kanban, KANBAN_LANE_NAME);
		
		// Then
		assertThat(result).isPresent();
		assertThat(result.get()
						 .getKanban()).isEqualTo(kanban);
		assertThat(result.get()
						 .getName()).isEqualTo(KANBAN_LANE_NAME);
	}
	
	@Test
	void findByKanbanAndName_kanban에속하지않은name1개존재_empty() {
		// Given
		Account account = insertDefaultAccount();
		
		Project project = insertDefaultProject(account);
		Kanban kanban = insertKanban(project);
		insertKanbanLane(kanban, "lane1");
		
		Project project2 = insertDefaultProject2(account);
		Kanban kanban2 = insertKanban(project2);
		insertKanbanLane(kanban2, "lane2");
		
		// When
		Optional<KanbanLane> result = kanbanLaneRepository.findByKanbanAndName(kanban, "lane2");
		
		// Then
		assertThat(result).isEmpty();
	}
	
	@Test
	void countByKanban_kanban에속한lane4개존재_return_4() {
		// Given
		Project project = insertDefaultProjectAndDefaultCaptain();
		Kanban kanban = insertKanban(project);
		insertKanbanLanes(kanban);
		
		// When
		Long result = kanbanLaneRepository.countByKanban(kanban);
		
		// Then
		assertThat(result).isEqualTo(4);
	}
	
	@Test
	void deleteByKanban_Kanban에속한lane4개존재_KanbanLane전체삭제() {
		// Given
		Project project = insertDefaultProjectAndDefaultCaptain();
		Kanban kanban = insertKanban(project);
		insertKanbanLanes(kanban);
		
		// When
		long result = kanbanLaneRepository.deleteByKanban(kanban);
		
		// Then
		assertThat(result).isEqualTo(4);
	}
	
	@Test
	void findByProjectAndKanbanLaneId_project에속한lane5개존재_1개조회() {
		// Given
		Project project = insertDefaultProjectAndDefaultCaptain();
		Kanban kanban = insertKanban(project);
		List<KanbanLane> kanbanLanes = insertKanbanLanes(kanban);
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
		Project project = insertDefaultProjectAndDefaultCaptain();
		Kanban kanban = insertKanban(project);
		
		// When
		Optional<KanbanLane> result = kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), 123L);
		
		// Then
		assertThat(result).isEmpty();
	}
	
}