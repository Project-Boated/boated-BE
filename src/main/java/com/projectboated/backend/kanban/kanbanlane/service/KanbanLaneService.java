package com.projectboated.backend.kanban.kanbanlane.service;

import com.projectboated.backend.project.aop.OnlyCaptain;
import com.projectboated.backend.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.project.repository.ProjectRepository;
import com.projectboated.backend.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.task.task.repository.TaskRepository;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanban.entity.exception.KanbanLaneChangeIndexOutOfBoundsException;
import com.projectboated.backend.kanban.kanban.entity.exception.KanbanLaneOriginalIndexOutOfBoundsException;
import com.projectboated.backend.kanban.kanban.repository.KanbanRepository;
import com.projectboated.backend.kanban.kanban.service.exception.KanbanNotFoundException;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.kanban.kanbanlane.service.dto.KanbanLaneUpdateRequest;
import com.projectboated.backend.kanban.kanbanlane.service.exception.KanbanLaneAlreadyExists5;
import com.projectboated.backend.kanban.kanbanlane.service.exception.KanbanLaneExists1Exception;
import com.projectboated.backend.kanban.kanbanlane.service.exception.KanbanLaneExistsTaskException;
import com.projectboated.backend.kanban.kanbanlane.service.exception.KanbanLaneNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanbanLaneService {

    private final ProjectRepository projectRepository;
    private final KanbanLaneRepository kanbanLaneRepository;
    private final TaskRepository taskRepository;
    private final KanbanRepository kanbanRepository;

    @OnlyCaptainOrCrew
    public List<KanbanLane> findByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        return kanbanLaneRepository.findByProject(project);
    }

    @Transactional
    @OnlyCaptain
    public void createNewLine(Long projectId, String name) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        Kanban kanban = kanbanRepository.findByProject(project)
                .orElseThrow(KanbanNotFoundException::new);

        if (kanbanLaneRepository.findByProject(project).size() >= 5) {
            throw new KanbanLaneAlreadyExists5();
        }

        KanbanLane kanbanLane = KanbanLane.builder()
                .name(name)
                .kanban(kanban)
                .project(project)
                .order(kanbanLaneRepository.findHighestOrder(project) + 1)
                .build();
        kanbanLaneRepository.save(kanbanLane);
    }

    @Transactional
    @OnlyCaptain
    public void updateKanbanLane(Long projectId, Long laneId, KanbanLaneUpdateRequest request) {
        KanbanLane kanbanLane = kanbanLaneRepository.findByProjectIdAndKanbanLaneId(projectId, laneId)
                .orElseThrow(KanbanLaneNotFoundException::new);
        kanbanLane.update(request);
    }

    @Transactional
    @OnlyCaptain
    public void deleteKanbanLane(Long projectId, Long kanbanLaneId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        List<KanbanLane> kanbanLanes = kanbanLaneRepository.findByProject(project);
        if (kanbanLanes.size() <= 1) {
            throw new KanbanLaneExists1Exception();
        }

        if (taskRepository.countByKanbanLaneId(kanbanLaneId) > 0) {
            throw new KanbanLaneExistsTaskException();
        }

        removeKanbanLane(kanbanLaneId, kanbanLanes);
        reorderKanbanLanes(kanbanLanes);
    }

    private void removeKanbanLane(Long kanbanLaneId, List<KanbanLane> kanbanLanes) {
        KanbanLane targetLane = null;
        for (KanbanLane kanbanLane : kanbanLanes) {
            if (Objects.equals(kanbanLane.getId(), kanbanLaneId)) {
                targetLane = kanbanLane;
                break;
            }
        }
        if (targetLane == null) {
            throw new KanbanLaneNotFoundException();
        }
        kanbanLanes.remove(targetLane);
        kanbanLaneRepository.delete(targetLane);
    }

    private void reorderKanbanLanes(List<KanbanLane> kanbanLanes) {
        for (int i = 0; i < kanbanLanes.size(); i++) {
            kanbanLanes.get(i).changeOrder(i);
        }
    }

    @Transactional
    @OnlyCaptainOrCrew
    public void changeKanbanLaneOrder(Long projectId, int originalIndex, int changeIndex) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        List<KanbanLane> kanbanLanes = kanbanLaneRepository.findByProject(project);
        if (originalIndex < 0 || originalIndex >= kanbanLanes.size()) {
            throw new KanbanLaneOriginalIndexOutOfBoundsException();
        }
        if (changeIndex < 0 || changeIndex >= kanbanLanes.size()) {
            throw new KanbanLaneChangeIndexOutOfBoundsException();
        }

        KanbanLane kanbanLane = kanbanLanes.remove(originalIndex);
        kanbanLanes.add(changeIndex, kanbanLane);

        reorderKanbanLanes(kanbanLanes);
    }
}
