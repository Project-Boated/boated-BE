package com.projectboated.backend.project.project.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.task.task.repository.TaskRepository;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanban.repository.KanbanRepository;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLaneType;
import com.projectboated.backend.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.project.project.aop.OnlyCaptain;
import com.projectboated.backend.project.project.entity.AccountProject;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.repository.AccountProjectRepository;
import com.projectboated.backend.project.project.repository.ProjectRepository;
import com.projectboated.backend.project.project.service.condition.GetMyProjectsCond;
import com.projectboated.backend.project.project.service.condition.ProjectUpdateCond;
import com.projectboated.backend.project.project.service.dto.MyProjectsDto;
import com.projectboated.backend.project.project.service.exception.ProjectNameSameInAccountException;
import com.projectboated.backend.project.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.project.projectchatting.chattingroom.domain.ProjectChattingRoom;
import com.projectboated.backend.project.projectchatting.chattingroom.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final AccountProjectRepository accountProjectRepository;
    private final KanbanRepository kanbanRepository;
    private final KanbanLaneRepository kanbanLaneRepository;
    private final TaskRepository taskRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    public Project findById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
    }

    @Transactional
    public Project save(Project project) {
        if (projectRepository.existsByNameAndCaptain(project.getName(), project.getCaptain())) {
            throw new ProjectNameSameInAccountException();
        }
        projectRepository.save(project);

        // create kanban
        Kanban kanban = kanbanRepository.save(new Kanban(project));

        // create kanban lane
        KanbanLaneType[] laneTypes = KanbanLaneType.values();
        for (int i = 0; i < laneTypes.length; i++) {
            kanbanLaneRepository.save(KanbanLane.builder()
                    .name(laneTypes[i].getName())
                    .order(i)
                    .project(project)
                    .kanban(kanban)
                    .build());
        }

        // create chatting
        chattingRoomRepository.save(ProjectChattingRoom.builder()
                .project(project)
                .build());

        return project;
    }

    @Transactional
    @OnlyCaptain
    public void update(Long accountId, Long projectId, ProjectUpdateCond cond) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (cond.getName() != null &&
                !cond.getName().equals(project.getName()) &&
                projectRepository.existsByNameAndCaptain(cond.getName(), account)) {
            throw new ProjectNameSameInAccountException();
        }

        project.update(cond);
    }

    @Transactional
    @OnlyCaptain
    public void delete(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        taskRepository.deleteByProject(project);
        kanbanLaneRepository.deleteByProject(project);
        kanbanRepository.deleteByProject(project);
        projectRepository.delete(project);
    }

    public MyProjectsDto getMyProjects(Long accountId, GetMyProjectsCond cond) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        List<Project> projects = projectRepository.getMyProjects(account, cond);
        return MyProjectsDto.builder()
                .page(cond.getPageable().getPageNumber())
                .size(projects.size())
                .hasNext(projects.size() == cond.getPageable().getPageSize())
                .projects(projects)
                .build();
    }

    public boolean isCrew(Project project, Account account) {
        return accountProjectRepository.countByCrewInProject(account, project) == 1;
    }

    public boolean isCaptainOrCrew(Project project, Account account) {
        return project.isCaptain(account) || isCrew(project, account);
    }

    public List<Project> findByAccountIdAndDate(Long accountId, LocalDateTime targetDate) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        LocalDateTime nextMonth = targetDate.plusMonths(1);

        List<Project> projects = projectRepository.findByCaptainAndDate(account, targetDate, nextMonth);

        for (AccountProject accountProject : accountProjectRepository.findByAccount(account)) {
            Project project = accountProject.getProject();
            if (project.getCreatedDate().isBefore(nextMonth) &&
                    (project.getDeadline().isEqual(targetDate) || project.getDeadline().isAfter(targetDate))
            ) {
                projects.add(accountProject.getProject());
            }
        }

        return projects;
    }
}
