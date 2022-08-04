package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.repository.AccountTaskRepository;
import com.projectboated.backend.domain.task.task.repository.TaskRepository;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.task.taskfile.repository.TaskFileRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;

import static com.projectboated.backend.common.data.BasicDataTask.*;

public class TaskFileRepositoryTest extends TaskRepositoryTest {

    @Autowired
    protected TaskFileRepository taskFileRepository;

    protected TaskFile insertTaskFile(Task task, UploadFile uploadFile) {
        return taskFileRepository.save(TaskFile.builder()
                .task(task)
                .uploadFile(uploadFile)
                .build());
    }

}
