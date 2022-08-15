package com.projectboated.backend.domain.task.taskfile.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import com.projectboated.backend.domain.task.task.service.exception.TaskNotFoundException;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.task.taskfile.repository.TaskFileRepository;
import com.projectboated.backend.domain.task.taskfile.service.exception.TaskFileNotFoundException;
import com.projectboated.backend.domain.task.taskfile.service.exception.UploadTaskFileAccessDeniedException;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.domain.uploadfile.repository.UploadFileRepository;
import com.projectboated.backend.infra.aws.AwsS3Service;
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

    @Transactional
    @OnlyCaptainOrCrew
    public TaskFile uploadFile(Long projectId, Long taskId, MultipartFile file) {
        Task task = taskRepository.findByProjectIdAndTaskId(projectId, taskId)
                .orElseThrow(TaskNotFoundException::new);

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName(file.getOriginalFilename())
                .saveFileName(UUID.randomUUID().toString())
                .mediaType(file.getContentType())
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
        taskFileRepository.delete(taskFile);
    }

    @OnlyCaptainOrCrew
    public TaskFile findById(Long projectId, Long taskFileId) {
        return taskFileRepository.findById(taskFileId)
                .orElseThrow(TaskFileNotFoundException::new);
    }
}
