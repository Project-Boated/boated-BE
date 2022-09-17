package com.projectboated.backend.web.kanban.kanbanlane.controller;

import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.web.config.WithMockAccount;
import com.projectboated.backend.web.kanban.kanbanlane.dto.UpdateKanbanLaneRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME2;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.web.kanban.kanbanlane.document.KanbanLaneDocument.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("KanbanLane : Controller 단위테스트")
class KanbanLaneControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void getLanes_lanes조회_조회정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 4);

        when(kanbanLaneService.findByProjectId(project.getId())).thenReturn(kanbanLanes);

        // When
        // Then
        mockMvc.perform(get("/api/projects/{projectId}/kanban/lanes", project.getId()))
                .andExpect(status().isOk())
                .andDo(documentLanesRetrieve());
    }

    @Test
    @WithMockAccount
    void updateKanbanLane_kanbanLane업데이트_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 4);

        when(kanbanLaneService.findByProjectId(project.getId())).thenReturn(kanbanLanes);

        // When
        // Then
        mockMvc.perform(put("/api/projects/{projectId}/kanban/lanes/{kanbanLaneId}", project.getId(), kanbanLanes.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(new UpdateKanbanLaneRequest(KANBAN_LANE_NAME2))))
                .andExpect(status().isOk())
                .andDo(documentUpdateKanbanLane());
    }

    @Test
    @WithMockAccount
    void changeKanbanLaneOrder_칸반lane옮기기_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        List<KanbanLane> kanbanLanes = createKanbanLanes(KANBAN_LANE_ID, project, kanban, 4);
        // When
        // Then
        mockMvc.perform(post("/api/projects/{projectId}/kanban/lanes/change/{originalIndex}/{changeIndex}", project.getId(), 0, 0))
                .andExpect(status().isOk())
                .andDo(documentKanbanLaneOrderChange());

        verify(kanbanLaneService).changeKanbanLaneOrder(any(), anyInt(), anyInt());
    }

}