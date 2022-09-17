package com.projectboated.backend.utils.basetest.repository;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.domain.projectvideo.repository.ProjectVideoRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectVideoRepositoryTest extends TaskLikeRepositoryTest {

    @Autowired
    protected ProjectVideoRepository projectVideoRepository;

    protected ProjectVideo insertProjectVideo(Project project, UploadFile uploadFile) {
        return projectVideoRepository.save(createProjectVideo(project, uploadFile));
    }

}
