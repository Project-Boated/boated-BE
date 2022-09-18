package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.taskfile.entity.TaskFile;
import com.projectboated.backend.task.taskfile.repository.TaskFileRepository;
import com.projectboated.backend.uploadfile.entity.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskFileRepositoryTest extends TaskRepositoryTest {

    @Autowired
    protected TaskFileRepository taskFileRepository;

    protected TaskFile insertTaskFile(Task task, UploadFile uploadFile) {
        return taskFileRepository.save(createTaskFile(task, uploadFile));
    }

}
