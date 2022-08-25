package com.projectboated.backend.domain.projectvideo.entity;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataProjectVideo.PROJECT_VIDEO_ID;
import static com.projectboated.backend.common.data.BasicDataUploadFile.MEDIATYPE;
import static com.projectboated.backend.common.data.BasicDataUploadFile.ORIGINAL_FILE_NAME;
import static org.assertj.core.api.Assertions.*;

@DisplayName("ProjectVideo : Entity 단위 테스트")
class ProjectVideoTest {

    @Test
    void constructor_AllParameter_SuccessConstruct(){
        // given
        Project project = Project.builder()
                .build();

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName(ORIGINAL_FILE_NAME)
                .mediaType(MEDIATYPE)
                .build();

        // when
        ProjectVideo projectVideo = new ProjectVideo(PROJECT_VIDEO_ID, project, uploadFile);

        // then
        assertThat(projectVideo.getProject()).isEqualTo(project);
        assertThat(projectVideo.getUploadFile()).isEqualTo(uploadFile);
    }

    @Test
    void getKey_getkey_return_key(){
        // given
        Project project = Project.builder()
                .id(PROJECT_ID)
                .build();

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName("original.txt")
                .saveFileName("save")
                .mediaType(MEDIATYPE)
                .build();

        ProjectVideo projectVideo = new ProjectVideo(PROJECT_VIDEO_ID, project, uploadFile);

        // when
        String result = projectVideo.getKey();

        // then
        assertThat(result).isEqualTo("projects/" + project.getId() + "/video/" + uploadFile.getSaveFileName() + "." + uploadFile.getExt());
    }

}