package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.DefaultKanbanLane;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLaneType;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.domain.kanban.kanban.repository.KanbanRepository;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.AccountProjectRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.condition.GetMyProjectsCond;
import com.projectboated.backend.domain.project.service.condition.ProjectUpdateCond;
import com.projectboated.backend.domain.project.service.dto.MyProjectsDto;
import com.projectboated.backend.domain.project.service.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;
    private final KanbanRepository kanbanRepository;
    private final KanbanLaneRepository kanbanLaneRepository;
    private final AccountProjectRepository accountProjectRepository;

    @Transactional
    public Project save(Project project) {
        if (projectRepository.existsByNameAndCaptain(project.getName(), project.getCaptain())) {
            throw new ProjectNameSameInAccountException(ErrorCode.PROJECT_NAME_EXISTS_IN_ACCOUNT);
        }

        Project newProject = projectRepository.save(project);

        Kanban newKanban = new Kanban(newProject);
        kanbanRepository.save(newKanban);

        for (KanbanLaneType kanbanLaneType : KanbanLaneType.values()) {
            String name = kanbanLaneType.name();
            KanbanLane kanbanLane = new DefaultKanbanLane(name, newProject, newKanban);
            kanbanLaneRepository.save(kanbanLane);
        }

        return newProject;
    }

    @Transactional
    public void update(Account account, Long projectId, ProjectUpdateCond projectUpdateCond) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!Objects.equals(project.getCaptain().getId(), account.getId())) {
            throw new ProjectUpdateAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        if(isSameProjectNameInAccount(projectUpdateCond, project)) {
            throw new ProjectNameSameInAccountException(ErrorCode.PROJECT_NAME_EXISTS_IN_ACCOUNT);
        }

        project.changeProjectInform(projectUpdateCond.getName(), projectUpdateCond.getDescription(), projectUpdateCond.getDeadline());
    }

    private boolean isSameProjectNameInAccount(ProjectUpdateCond projectUpdateCond, Project project) {
        return !Objects.equals(project.getName(), projectUpdateCond.getName()) &&
                projectRepository.existsByName(projectUpdateCond.getName());
    }
    public List<Account> findAllCrews(Account account, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if(!isCaptain(account, project) && !isCrew(account, projectId)) {
            throw new ProjectAccessDeniedException.ProjectFindCrewsAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        return accountProjectRepository.findCrewsFromProject(project);
    }

    @Transactional
    public void deleteCrew(Account account, Long projectId, String crewNickname) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(account, project)) {
            throw new ProjectCaptainUpdateDenied(ErrorCode.COMMON_ACCESS_DENIED);
        }

        Account crew = accountRepository.findByNickname(crewNickname)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND_BY_USERNAME));

        accountProjectRepository.delete(project, crew);
    }

    @Transactional
    public void terminateProject(Account account, Long projectId) {
        Account captain = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(captain, project)) {
            throw new ProjectUpdateAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        project.terminateProject();
    }

    @Transactional
    public void cancelTerminateProject(Account account, Long projectId) {
        Account captain = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(captain, project)) {
            throw new ProjectUpdateAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        project.cancelTerminateProject();
    }

    public Project findById(Long projectId, Account account) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));
    }

    @Transactional
    public void delete(Account account, Long projectId) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(findAccount, project)) {
            throw new ProjectDeleteAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        kanbanLaneRepository.deleteByProject(project);
        kanbanRepository.deleteByProject(project);
        projectRepository.delete(project);
    }

    public boolean isCrew(Account account, Long projectId) {
        return accountProjectRepository.countByCrewInProject(account.getId(), projectId) == 1;
    }

    public boolean isCaptain(Account account, Project project) {
        return project.getCaptain().getId() == account.getId();
    }

    public MyProjectsDto getMyProjects(Account account, GetMyProjectsCond cond) {
        List<Project> projects = projectRepository.getMyProjects(account, cond);
        return MyProjectsDto.builder()
                .page(cond.getPageable().getPageNumber())
                .size(cond.getPageable().getPageSize())
                .hasNext(projects.size()==cond.getPageable().getPageSize())
                .projects(projects)
                .build();
    }
}
