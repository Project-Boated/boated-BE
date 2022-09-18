package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.project.projectvideo.repository.ProjectVideoRepository;
import com.projectboated.backend.uploadfile.entity.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectVideoRepositoryTest extends TaskLikeRepositoryTest {

    @Autowired
    protected ProjectVideoRepository projectVideoRepository;

    protected ProjectVideo insertProjectVideo(Project project, UploadFile uploadFile) {
        return projectVideoRepository.save(createProjectVideo(project, uploadFile));
    }

}
