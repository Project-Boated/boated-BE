package com.projectboated.backend.domain.task.taskfile.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TaskFileRepositoryTest extends RepositoryTest {

    @Test
    void findByTask_아무것도없는경우_return_empty() {
        // Given
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);
        Task task = insertDefaultTask(project);
        UploadFile uploadFile = insertDefaultUploadFile();

        // When
        List<TaskFile> result = taskFileRepository.findByTask(task);

        // Then
        assertThat(result).hasSize(0);
    }

    @Test
    void findByTask_2개있는경우_return_2개() {
        // Given
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);
        Task task = insertDefaultTask(project);
        UploadFile uploadFile = insertDefaultUploadFile();
        TaskFile taskFile = insertTaskFile(task, uploadFile);
        TaskFile taskFile2 = insertTaskFile(task, uploadFile);

        // When
        List<TaskFile> result = taskFileRepository.findByTask(task);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void deleteByTask_2개있는경우_2개없앰() {
        // Given
        Account account = insertDefaultAccount();
        Project project = insertDefaultProject(account);
        Task task = insertDefaultTask(project);
        UploadFile uploadFile = insertDefaultUploadFile();
        TaskFile taskFile = insertTaskFile(task, uploadFile);
        TaskFile taskFile2 = insertTaskFile(task, uploadFile);

        // When
        taskFileRepository.deleteByTask(task);

        // Then
        assertThat(taskFileRepository.findByTask(task)).isEmpty();
    }

}