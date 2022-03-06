package my.sleepydeveloper.projectcompass.domain.project.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.account.exception.NotFoundAccountException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountProjectRepository;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNotFoundException;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectUpdateAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.exception.SameProjectNameInAccountExists;
import my.sleepydeveloper.projectcompass.domain.project.exception.UpdateCaptainAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import my.sleepydeveloper.projectcompass.domain.project.vo.ProjectUpdateCondition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;
    private final AccountProjectRepository accountProjectRepository;

    @Transactional
    public Project save(Project project) {
        if (projectRepository.existsByNameAndCaptain(project.getName(), project.getCaptain())) {
            throw new SameProjectNameInAccountExists(ErrorCode.SameProjectNameInAccountExists);
        }

        return projectRepository.save(project);
    }

    @Transactional
    public void update(Account account, Long projectId, ProjectUpdateCondition projectUpdateCondition) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (project.getCaptain().getId() != account.getId()) {
            throw new ProjectUpdateAccessDenied(ErrorCode.COMMON_ACCESS_DENIED);
        }

        if (projectRepository.countByNameAndCaptainAndNotProject(projectUpdateCondition.getName(), account, project) > 0) {
            throw new SameProjectNameInAccountExists(ErrorCode.SameProjectNameInAccountExists);
        }

        project.changeProjectInform(projectUpdateCondition.getName(), projectUpdateCondition.getDescription());
    }

    public List<Project> findAllByAccount(Account account) {
        return projectRepository.findAllByCaptain(account);
    }

    public List<Account> findAllCrews(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        return accountProjectRepository.findCrewsFromProject(project);
    }

    @Transactional
    public Account updateCaptain(Account account, String newCaptainUsername, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (project.getCaptain().getId() != account.getId()) {
            throw new UpdateCaptainAccessDenied(ErrorCode.PROJECT_UPDATE_CAPTAIN_ACCESS_DENIED);
        }

        Account newCaptain = accountRepository.findByUsername(newCaptainUsername)
                .orElseThrow(() -> new NotFoundAccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        accountProjectRepository.save(new AccountProject(account, project));
        accountProjectRepository.delete(project, newCaptain);
        project.changeCaptain(newCaptain);

        return newCaptain;
    }

}
