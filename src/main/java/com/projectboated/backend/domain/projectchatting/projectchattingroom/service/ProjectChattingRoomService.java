package com.projectboated.backend.domain.projectchatting.projectchattingroom.service;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.projectchatting.projectchattingroom.domain.ProjectChattingRoom;
import com.projectboated.backend.domain.projectchatting.projectchattingroom.repository.ProjectChattingRoomRepository;
import com.projectboated.backend.domain.projectchatting.projectchattingroom.service.exception.ProjectChattingRoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectChattingRoomService {

    private final ProjectRepository projectRepository;
    private final ProjectChattingRoomRepository projectChattingRoomRepository;

    public ProjectChattingRoom findByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        return projectChattingRoomRepository.findByProject(project)
                .orElseThrow(ProjectChattingRoomNotFoundException::new);
    }
}
