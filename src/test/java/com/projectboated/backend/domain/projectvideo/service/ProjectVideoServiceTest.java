package com.projectboated.backend.domain.projectvideo.service;

import com.projectboated.backend.common.basetest.ServiceTest;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.domain.projectvideo.repository.ProjectVideoRepository;
import com.projectboated.backend.domain.projectvideo.service.exception.ProjectVideoIsPresentException;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.domain.uploadfile.repository.UploadFileRepository;
import com.projectboated.backend.infra.aws.AwsS3Service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.util.Optional;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataUploadFile.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("ProjectVideo : Service 단위 테스트")
class ProjectVideoServiceTest extends ServiceTest {

    @InjectMocks
    ProjectVideoService projectVideoService;

    @Mock
    AwsS3Service awsS3Service;

    @Mock
    ProjectVideoRepository projectVideoRepository;
    @Mock
    UploadFileRepository uploadFileRepository;
    @Mock
    ProjectRepository projectRepository;

    @Test
    void save_notExistsProject_throwException(){
        // given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> projectVideoService.save(PROJECT_ID, null))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void save_isPresentProjectVideo_throwException(){
        // given
        Project project = createProjectAndCaptain(PROJECT_ID, ACCOUNT_ID);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);
        ProjectVideo projectVideo = createProjectVideo(project, uploadFile);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectVideoRepository.findByProject(project)).thenReturn(Optional.of(projectVideo));

        // when
        // then
        assertThatThrownBy(() -> projectVideoService.save(PROJECT_ID, null))
                .isInstanceOf(ProjectVideoIsPresentException.class);
    }

    @Test
    void save_goodParameter_successSave(){
        // given
        Project project = createProjectAndCaptain(PROJECT_ID, ACCOUNT_ID);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);
        ProjectVideo projectVideo = createProjectVideo(project, uploadFile);
        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MEDIATYPE, "file".getBytes());

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectVideoRepository.findByProject(project)).thenReturn(Optional.empty());

        // when
        ProjectVideo result = projectVideoService.save(PROJECT_ID, multipartFile);

        // then
        verify(uploadFileRepository).save(any());
        verify(projectVideoRepository).save(any());
        verify(awsS3Service).uploadFile(any(), any(MultipartFile.class));
    }

}