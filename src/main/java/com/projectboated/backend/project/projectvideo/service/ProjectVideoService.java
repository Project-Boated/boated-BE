package com.projectboated.backend.project.projectvideo.service;

import com.projectboated.backend.project.projectvideo.repository.ProjectVideoRepository;
import com.projectboated.backend.project.projectvideo.service.exception.ProjectVideoNotFoundException;
import com.projectboated.backend.uploadfile.entity.UploadFile;
import com.projectboated.backend.uploadfile.repository.UploadFileRepository;
import com.projectboated.backend.infra.aws.AwsS3Service;
import com.projectboated.backend.project.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.repository.ProjectRepository;
import com.projectboated.backend.project.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.project.projectvideo.entity.ProjectVideo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectVideoService {

    private final ProjectVideoRepository projectVideoRepository;
    private final UploadFileRepository uploadFileRepository;
    private final ProjectRepository projectRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    @OnlyCaptainOrCrew
    public ProjectVideo save(Long projectId, MultipartFile multipartFile) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        projectVideoRepository.findByProject(project)
                .ifPresent((pv) -> {
                    uploadFileRepository.delete(pv.getUploadFile());
                    projectVideoRepository.delete(pv);
                });

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName(multipartFile.getOriginalFilename())
                .saveFileName(UUID.randomUUID().toString())
                .mediaType(multipartFile.getContentType())
                .build();
        uploadFileRepository.save(uploadFile);

        ProjectVideo projectVideo = ProjectVideo.builder()
                .project(project)
                .uploadFile(uploadFile)
                .build();
        projectVideoRepository.save(projectVideo);

        awsS3Service.uploadFile(projectVideo.getKey(), multipartFile);

        return projectVideo;
    }

    @OnlyCaptainOrCrew
    public ProjectVideo findByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        return projectVideoRepository.findByProject(project)
                .orElseThrow(ProjectVideoNotFoundException::new);
    }

    @OnlyCaptainOrCrew
    @Transactional
    public void deleteByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        ProjectVideo projectVideo = projectVideoRepository.findByProject(project)
                .orElseThrow(ProjectVideoNotFoundException::new);

        uploadFileRepository.delete(projectVideo.getUploadFile());
        projectVideoRepository.delete(projectVideo);
    }
}
