package com.projectboated.backend.web.projectvideo;

import com.google.common.net.MediaType;
import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.web.config.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.web.project.controller.document.ProjectTerminateDocument.documentProjectTerminate;
import static com.projectboated.backend.web.projectvideo.document.ProjectVideoDocument.documentProjectVideoCreate;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ProjectVideo : Controller 단위테스트")
class ProjectVideoControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void createProjectVideo_uploadVideo_success() throws Exception {
        // Given
        // When
        // Then
        mockMvc.perform(multipart("/api/projects/{projectId}/video", PROJECT_ID)
                        .file(new MockMultipartFile("file", "file.txt",
                                MediaType.JPEG.toString(), "profileFileImage".getBytes())))
                .andExpect(status().isOk())
                .andDo(documentProjectVideoCreate());

        verify(projectVideoService).save(any(), any());
    }

}