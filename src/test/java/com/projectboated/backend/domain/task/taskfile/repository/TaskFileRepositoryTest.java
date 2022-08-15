package com.projectboated.backend.domain.task.taskfile.repository;

import com.projectboated.backend.common.basetest.RepositoryTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.TASK_ID;
import static com.projectboated.backend.common.data.BasicDataTaskFile.TASK_FILE_ID;
import static com.projectboated.backend.common.data.BasicDataTaskFile.TASK_FILE_ID2;
import static com.projectboated.backend.common.data.BasicDataUploadFile.UPLOAD_FILE_ID;
import static org.assertj.core.api.Assertions.*;

class TaskFileRepositoryTest extends RepositoryTest {

    @Test
    void findByTask_아무것도없는경우_return_empty() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);

        // When
        List<TaskFile> result = taskFileRepository.findByTask(task);

        // Then
        assertThat(result).hasSize(0);
    }

    @Test
    void findByTask_2개있는경우_return_2개() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        UploadFile uploadFile = insertUploadFile();
        insertTaskFile(task, uploadFile);
        insertTaskFile(task, uploadFile);

        // When
        List<TaskFile> result = taskFileRepository.findByTask(task);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    void deleteByTask_2개있는경우_2개없앰() {
        // Given
        Account account = insertAccount();
        Project project = insertProject(account);
        Kanban kanban = insertKanban(project);
        KanbanLane kanbanLane = insertKanbanLane(project, kanban);
        Task task = insertTask(project, kanbanLane);
        UploadFile uploadFile = insertUploadFile();
        insertTaskFile(task, uploadFile);
        insertTaskFile(task, uploadFile);

        // When
        taskFileRepository.deleteByTask(task);

        // Then
        assertThat(taskFileRepository.findByTask(task)).isEmpty();
    }

}