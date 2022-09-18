package com.projectboated.backend.domain.task.taskfile.repository;

import com.projectboated.backend.utils.base.RepositoryTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.project.entity.Project;
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