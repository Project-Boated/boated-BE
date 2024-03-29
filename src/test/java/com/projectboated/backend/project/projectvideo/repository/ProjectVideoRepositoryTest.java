package com.projectboated.backend.project.projectvideo.repository;

import com.projectboated.backend.uploadfile.entity.UploadFile;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.utils.base.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProjectVideo : Persistence 단위 테스트")
class ProjectVideoRepositoryTest extends RepositoryTest {

    @Test
    void findByProject_videoExists1_return_1video() {
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

    @Test
    void findByProjectId_video가1개존재_return_1video() {
        // given
        Project project = insertProjectAndCaptain();
        UploadFile uploadFile = insertUploadFile();
        insertProjectVideo(project, uploadFile);

        // when
        Optional<ProjectVideo> result = projectVideoRepository.findByProjectId(project.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getProject()).isEqualTo(project);
        assertThat(result.get().getUploadFile()).isEqualTo(uploadFile);
    }


}