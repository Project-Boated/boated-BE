package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ProjectChattingRoom;

public class BaseProjectChattingRoomTest extends BaseChattingRoomTest {

    protected ProjectChattingRoom createProjectChattingRoom(Long id, Project project) {
        return ProjectChattingRoom.builder()
                .id(id)
                .project(project)
                .build();
    }

    protected ProjectChattingRoom createProjectChattingRoom(Project project) {
        return ProjectChattingRoom.builder()
                .project(project)
                .build();
    }

}
