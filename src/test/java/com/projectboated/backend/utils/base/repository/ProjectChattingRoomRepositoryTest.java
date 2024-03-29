package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.projectchatting.chattingroom.domain.ProjectChattingRoom;
import com.projectboated.backend.project.projectchatting.chattingroom.repository.ProjectChattingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectChattingRoomRepositoryTest extends ChattingRoomRepositoryTest {

    @Autowired
    protected ProjectChattingRoomRepository projectChattingRoomRepository;

    protected ProjectChattingRoom insertProjectChattingRoom(Project project) {
        return projectChattingRoomRepository.save(createProjectChattingRoom(project));
    }

}
