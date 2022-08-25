package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;

public class BaseProjectVideoTest extends BaseTaskLikeTest {

    protected ProjectVideo createProjectVideo(Project project, UploadFile uploadFile) {
        return ProjectVideo.builder()
                .project(project)
                .uploadFile(uploadFile)
                .build();
    }

    protected ProjectVideo createProjectVideo(Long id, Project project, UploadFile uploadFile) {
        return ProjectVideo.builder()
                .id(id)
                .project(project)
                .uploadFile(uploadFile)
                .build();
    }

}
