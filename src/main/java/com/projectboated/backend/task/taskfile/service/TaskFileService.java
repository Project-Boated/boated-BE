package com.projectboated.backend.task.taskfile.service;

import com.projectboated.backend.uploadfile.entity.UploadFile;
import com.projectboated.backend.uploadfile.repository.UploadFileRepository;
import com.projectboated.backend.infra.aws.AwsS3Service;
import com.projectboated.backend.project.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.repository.ProjectRepository;
import com.projectboated.backend.project.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.task.repository.TaskRepository;
import com.projectboated.backend.task.task.service.exception.TaskNotFoundException;
import com.projectboated.backend.task.taskfile.entity.TaskFile;
import com.projectboated.backend.task.taskfile.repository.TaskFileRepository;
import com.projectboated.backend.task.taskfile.service.exception.TaskFileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskFileService {

    private final AwsS3Service s3Service;
    private final TaskRepository taskRepository;
    private final TaskFileRepository taskFileRepository;
    private final UploadFileRepository uploadFileRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    @OnlyCaptainOrCrew
    public TaskFile uploadFile(Long projectId, Long taskId, MultipartFile file) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        project.addTotalFileSize(file.getSize());

        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName(file.getOriginalFilename())
                .saveFileName(UUID.randomUUID().toString())
                .mediaType(file.getContentType())
                .fileSize(file.getSize())
                .build();
        uploadFileRepository.save(uploadFile);

        TaskFile taskFile = TaskFile.builder()
                .task(task)
                .uploadFile(uploadFile)
                .build();
        taskFileRepository.save(taskFile);

        s3Service.uploadFile(taskFile.getKey(), file);
        return taskFile;
    }

    @OnlyCaptainOrCrew
    @Transactional
    public void delete(Long projectId, Long taskId, Long taskFileId) {
        TaskFile taskFile = taskFileRepository.findById(taskFileId)
                .orElseThrow(TaskFileNotFoundException::new);

        UploadFile uploadFile = taskFile.getUploadFile();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        project.minusTotalFileSize(uploadFile.getFileSize());

        taskFileRepository.delete(taskFile);
        s3Service.deleteFile(taskFile.getKey());
    }

    @OnlyCaptainOrCrew
    public TaskFile findById(Long projectId, Long taskFileId) {
        return taskFileRepository.findById(taskFileId)
                .orElseThrow(TaskFileNotFoundException::new);
    }
}
