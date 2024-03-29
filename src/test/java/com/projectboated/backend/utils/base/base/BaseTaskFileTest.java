package com.projectboated.backend.utils.base.base;

import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.taskfile.entity.TaskFile;
import com.projectboated.backend.uploadfile.entity.UploadFile;

import java.time.LocalDateTime;

public class BaseTaskFileTest extends BaseInvitationTest {

    protected TaskFile createTaskFile(Task task, UploadFile uploadFile) {
        return TaskFile.builder()
                .task(task)
                .uploadFile(uploadFile)
                .build();
    }


    protected TaskFile createTaskFile(Long id, Task task, UploadFile uploadFile) {
        TaskFile taskFile = TaskFile.builder()
                .id(id)
                .task(task)
                .uploadFile(uploadFile)
                .build();
        taskFile.changeCreatedDate(LocalDateTime.now());
        return taskFile;
    }


}
