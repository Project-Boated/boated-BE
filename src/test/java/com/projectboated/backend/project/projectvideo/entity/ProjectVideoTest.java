package com.projectboated.backend.project.projectvideo.entity;

import com.projectboated.backend.project.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.uploadfile.entity.UploadFile;
import com.projectboated.backend.project.project.entity.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataProjectVideo.*;
import static com.projectboated.backend.utils.data.BasicDataUploadFile.MEDIATYPE;
import static com.projectboated.backend.utils.data.BasicDataUploadFile.ORIGINAL_FILE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProjectVideo : Entity 단위 테스트")
class ProjectVideoTest {

    @Test
    void constructor_AllParameter_SuccessConstruct() {
        // given
        Project project = Project.builder()
                .build();

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName(ORIGINAL_FILE_NAME)
                .mediaType(MEDIATYPE)
                .build();

        // when
        ProjectVideo projectVideo = new ProjectVideo(PROJECT_VIDEO_ID, project, uploadFile, PROJECT_VIDEO_DESCRIPTION);

        // then
        assertThat(projectVideo.getProject()).isEqualTo(project);
        assertThat(projectVideo.getUploadFile()).isEqualTo(uploadFile);
    }

    @Test
    void getKey_getkey_return_key() {
        // given
        Project project = Project.builder()
                .id(PROJECT_ID)
                .build();

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName("original.txt")
                .saveFileName("save")
                .mediaType(MEDIATYPE)
                .build();

        ProjectVideo projectVideo = new ProjectVideo(PROJECT_VIDEO_ID, project, uploadFile, PROJECT_VIDEO_DESCRIPTION);

        // when
        String result = projectVideo.getKey();

        // then
        assertThat(result).isEqualTo("projects/" + project.getId() + "/video/" + uploadFile.getSaveFileName() + "." + uploadFile.getExt());
    }

    @Test
    void changeDescription_정상요청_정상change(){
        // Given
        Project project = Project.builder()
                .id(PROJECT_ID)
                .build();

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName("original.txt")
                .saveFileName("save")
                .mediaType(MEDIATYPE)
                .build();

        ProjectVideo projectVideo = new ProjectVideo(PROJECT_VIDEO_ID, project, uploadFile, PROJECT_VIDEO_DESCRIPTION);

        // When
        projectVideo.changeDescription(PROJECT_VIDEO_DESCRIPTION2);

        // Then
        assertThat(projectVideo.getDescription()).isEqualTo(PROJECT_VIDEO_DESCRIPTION2);
    }

}