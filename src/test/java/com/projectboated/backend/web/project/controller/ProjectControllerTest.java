package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.web.config.WithMockAccount;
import com.projectboated.backend.web.project.dto.request.CreateProjectRequest;
import com.projectboated.backend.web.project.dto.request.PatchProjectRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.TASK_ID;
import static com.projectboated.backend.web.project.controller.document.ProjectDocument.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Project : Controller 단위테스트")
class ProjectControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void getProject_프로젝트프로필조회_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);

        when(projectService.findById(project.getId())).thenReturn(project);
        when(taskService.taskSize(project.getId())).thenReturn(3L);

        // When
        // Then
        mockMvc.perform(get("/api/projects/{projectId}", project.getId()))
                .andExpect(status().isOk())
                .andDo(documentProjectRetrieve());
    }

    @Test
    @WithMockAccount
    void createProject_프로젝트생성_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);

        when(projectService.save(any())).thenReturn(project);

        // When
        // Then
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(new CreateProjectRequest(PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE))))
                .andExpect(status().isOk())
                .andDo(documentProjectCreate());

        verify(projectService).save(any());
    }

    @Test
    @WithMockAccount
    void patchProject_모든필드업데이트_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);

        // When
        // Then
        mockMvc.perform(patch("/api/projects/{projectId}", project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(new PatchProjectRequest(PROJECT_NAME2, PROJECT_DESCRIPTION2, PROJECT_DEADLINE2))))
                .andExpect(status().isOk())
                .andDo(documentProjectUpdate());

        verify(projectService).update(any(), any(), any());
    }

    @Test
    @WithMockAccount
    void deleteProject_프로젝트삭제_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);

        // When
        // Then
        mockMvc.perform(delete("/api/projects/{projectId}", project.getId()))
                .andExpect(status().isOk())
                .andDo(documentProjectDelete());

        verify(projectService).delete(any());
    }

}