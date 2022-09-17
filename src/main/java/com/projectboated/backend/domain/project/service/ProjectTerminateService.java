package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.domain.project.aop.OnlyCaptain;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectTerminateService {

    private final ProjectRepository projectRepository;

    @Transactional
    @OnlyCaptain
    public void terminateProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));
        project.terminate();
    }

    @Transactional
    @OnlyCaptain
    public void cancelTerminateProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));
        project.cancelTerminate();
    }

}
