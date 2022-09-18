package com.projectboated.backend.projectvideo.controller;

import com.google.common.net.MediaType;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.utils.base.ControllerTest;
import com.projectboated.backend.utils.controller.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import static com.projectboated.backend.projectvideo.controller.document.ProjectVideoDocument.*;
import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataProjectVideo.PROJECT_VIDEO_ID;
import static com.projectboated.backend.utils.data.BasicDataUploadFile.UPLOAD_FILE_ID;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ProjectVideo : Controller 단위테스트")
class ProjectVideoControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void createProjectVideo_uploadVideo_success() throws Exception {
        // Given
        // When
        // Then
        MockMultipartHttpServletRequestBuilder builder = multipart("/api/projects/{projectId}/video", PROJECT_ID);
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(builder
                        .file(new MockMultipartFile("file", "file.txt",
                                MediaType.JPEG.toString(), "profileFileImage".getBytes())))
                .andExpect(status().isOk())
                .andDo(documentProjectVideoCreate());

        verify(projectVideoService).save(any(), any());
    }

    @Test
    @WithMockAccount
    void getProjectVideo_존재하는video_return성공() throws Exception {
        // Give
        Project project = createProjectAndCaptain(PROJECT_ID, ACCOUNT_ID);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);
        ProjectVideo projectVideo = createProjectVideo(PROJECT_VIDEO_ID, project, uploadFile);

        when(projectVideoService.findByProjectId(project.getId())).thenReturn(projectVideo);
        when(awsS3Service.getBytes(any())).thenReturn("file".getBytes());

        // When
        // Then
        mockMvc.perform(get("/api/projects/{projectId}/video", PROJECT_ID))
                .andExpect(status().isOk())
                .andDo(documentProjectVideoRetrieve());
    }

    @Test
    @WithMockAccount
    void deleteProjectVideo_정상요청_delete성공() throws Exception {
        // Give
        // When
        // Then
        mockMvc.perform(delete("/api/projects/{projectId}/video", PROJECT_ID))
                .andExpect(status().isOk())
                .andDo(documentProjectVideoDelete());

        verify(projectVideoService).deleteByProjectId(PROJECT_ID);
    }

}