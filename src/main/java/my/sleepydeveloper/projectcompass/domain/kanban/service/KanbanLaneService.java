package my.sleepydeveloper.projectcompass.domain.kanban.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.kanban.entity.KanbanLane;
import my.sleepydeveloper.projectcompass.domain.kanban.exception.KanbanLaneAlreadyExists5;
import my.sleepydeveloper.projectcompass.domain.kanban.exception.KanbanLaneSaveAccessDeniedException;
import my.sleepydeveloper.projectcompass.domain.kanban.repository.KanbanLaneRepository;
import my.sleepydeveloper.projectcompass.domain.kanban.repository.KanbanRepository;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNotFoundException;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanbanLaneService {

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final KanbanRepository kanbanRepository;
    private final KanbanLaneRepository kanbanLaneRepository;

    @Transactional
    public void save(Account account, Long projectId, KanbanLane kanbanLane) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(account, project)) {
            throw new KanbanLaneSaveAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        if (kanbanLaneRepository.countByProject(project) >= 5) {
            throw new KanbanLaneAlreadyExists5(ErrorCode.KANBAN_LANE_EXISTS_UPPER_5);
        }

        kanbanLane.setProject(project);
        kanbanLane.setKanban(project.getKanban());
        kanbanLaneRepository.save(kanbanLane);
    }

    private boolean isCaptain(Account account, Project project) {
        return project.getCaptain().getId() == account.getId();
    }
}
