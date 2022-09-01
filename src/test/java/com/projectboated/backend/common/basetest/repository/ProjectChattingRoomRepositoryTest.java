package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectchatting.projectchattingroom.domain.ProjectChattingRoom;
import com.projectboated.backend.domain.projectchatting.projectchattingroom.repository.ProjectChattingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectChattingRoomRepositoryTest extends ChattingRoomRepositoryTest {

    @Autowired
    protected ProjectChattingRoomRepository projectChattingRoomRepository;

    protected ProjectChattingRoom insertProjectChattingRoom(Long id, Project project) {
        return projectChattingRoomRepository.save(createProjectChattingRoom(id, project));
    }

    protected ProjectChattingRoom insertProjectChattingRoom(Project project) {
        return projectChattingRoomRepository.save(createProjectChattingRoom(project));
    }

}
