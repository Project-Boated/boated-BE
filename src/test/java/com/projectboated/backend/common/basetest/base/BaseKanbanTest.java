package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.project.entity.Project;

import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;

public class BaseKanbanTest extends BaseProjectTest{

    protected Kanban createKanban(Long id, Project project) {
        return Kanban.builder()
                .id(id)
                .project(project)
                .build();
    }

    protected Kanban createKanban(Project project) {
        return Kanban.builder()
                .project(project)
                .build();
    }

}
