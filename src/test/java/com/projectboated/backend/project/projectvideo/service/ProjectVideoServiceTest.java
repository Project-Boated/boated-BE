package com.projectboated.backend.project.projectvideo.service;

import com.projectboated.backend.uploadfile.entity.UploadFile;
import com.projectboated.backend.uploadfile.repository.UploadFileRepository;
import com.projectboated.backend.infra.aws.AwsS3Service;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.repository.ProjectRepository;
import com.projectboated.backend.project.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.project.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.project.projectvideo.repository.ProjectVideoRepository;
import com.projectboated.backend.project.projectvideo.service.exception.ProjectVideoNotFoundException;
import com.projectboated.backend.utils.base.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.utils.data.BasicDataUploadFile.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void save_찾을수없는프로젝트_예외발생() {
        // given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> projectVideoService.save(PROJECT_ID, null))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void save_이미존재하는projectVideo_이미존재하는projectvideo삭제() {
        // given
        Project project = createProjectAndCaptain(PROJECT_ID, ACCOUNT_ID);
        UploadFile uploadFile = createUploadFile(UPLOAD_FILE_ID);
        ProjectVideo projectVideo = createProjectVideo(project, uploadFile);
        MockMultipartFile multipartFile = new MockMultipartFile("file", ORIGINAL_FILE_NAME, MEDIATYPE, "file".getBytes());

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectVideoRepository.findByProject(project)).thenReturn(Optional.of(projectVideo));

        // when
        ProjectVideo result = projectVideoService.save(PROJECT_ID, multipartFile);

        // then
        verify(uploadFileRepository).delete(uploadFile);
        verify(projectVideoRepository).delete(projectVideo);
        verify(uploadFileRepository).save(any());
        verify(projectVideoRepository).save(any());
        verify(awsS3Service).uploadFile(any(), any(MultipartFile.class));
    }

    @Test
    void save_존재하지않는projectVideo_save성공() {
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

    @Test
    void findByProjectId_존재하지않는프로젝트_예외발생() {
        // given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> projectVideoService.findByProjectId(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void findByProjectId_존재하지않는ProjectVideo_예외발생() {
        // given
        Project project = createProjectAndCaptain();

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectVideoRepository.findByProject(project)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> projectVideoService.findByProjectId(project.getId()))
                .isInstanceOf(ProjectVideoNotFoundException.class);
    }

    @Test
    void findByProjectId_정상요청_1개성공() {
        // given
        Project project = createProjectAndCaptain();
        UploadFile uploadFile = createUploadFile();
        ProjectVideo projectVideo = createProjectVideo(project, uploadFile);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectVideoRepository.findByProject(project)).thenReturn(Optional.of(projectVideo));

        // when
        ProjectVideo result = projectVideoService.findByProjectId(project.getId());

        // then
        assertThat(result).isEqualTo(projectVideo);
    }

    @Test
    void deleteByProjectId_존재하지않는프로젝트_예외발생() {
        // given
        when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> projectVideoService.deleteByProjectId(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void deleteByProjectId_존재하지않는ProjectVideo_예외발생() {
        // given
        Project project = createProjectAndCaptain();

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectVideoRepository.findByProject(project)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> projectVideoService.deleteByProjectId(project.getId()))
                .isInstanceOf(ProjectVideoNotFoundException.class);
    }

    @Test
    void deleteByProjectId_정상요청_delete성공() {
        // given
        Project project = createProjectAndCaptain();
        UploadFile uploadFile = createUploadFile();
        ProjectVideo projectVideo = createProjectVideo(project, uploadFile);

        when(projectRepository.findById(project.getId())).thenReturn(Optional.of(project));
        when(projectVideoRepository.findByProject(project)).thenReturn(Optional.of(projectVideo));

        // when
        projectVideoService.deleteByProjectId(project.getId());

        // then
        verify(uploadFileRepository).delete(projectVideo.getUploadFile());
        verify(projectVideoRepository).delete(projectVideo);
    }

}