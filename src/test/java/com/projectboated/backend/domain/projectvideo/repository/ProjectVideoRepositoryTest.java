package com.projectboated.backend.domain.projectvideo.repository;

import com.projectboated.backend.utils.basetest.RepositoryTest;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ProjectVideo : Persistence 단위 테스트")
class ProjectVideoRepositoryTest extends RepositoryTest {

    @Test
    void findByProject_videoExists1_return_1video(){
        // given
        Project project = insertProjectAndCaptain();
        UploadFile uploadFile = insertUploadFile();
        insertProjectVideo(project, uploadFile);

        // when
        Optional<ProjectVideo> result = projectVideoRepository.findByProject(project);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getProject()).isEqualTo(project);
        assertThat(result.get().getUploadFile()).isEqualTo(uploadFile);
    }



}