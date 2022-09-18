package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.projectvideo.repository.ProjectVideoRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectVideoRepositoryTest extends TaskLikeRepositoryTest {

    @Autowired
    protected ProjectVideoRepository projectVideoRepository;

    protected ProjectVideo insertProjectVideo(Project project, UploadFile uploadFile) {
        return projectVideoRepository.save(createProjectVideo(project, uploadFile));
    }

}
