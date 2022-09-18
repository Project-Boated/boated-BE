package com.projectboated.backend.utils.base.base;

import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.uploadfile.entity.UploadFile;

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
