package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.project.entity.Project;

public class BaseKanbanTest extends BaseProjectTest{

    protected Kanban createKanban(Project project) {
        return Kanban.builder()
                .project(project)
                .build();
    }

}
