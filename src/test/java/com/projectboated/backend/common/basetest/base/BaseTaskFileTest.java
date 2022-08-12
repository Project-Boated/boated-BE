package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;

public class BaseTaskFileTest extends BaseInvitationTest{

    protected TaskFile createTaskFile(Task task, UploadFile uploadFile) {
        return TaskFile.builder()
                .task(task)
                .uploadFile(uploadFile)
                .build();
    }


}
