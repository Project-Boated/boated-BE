package com.projectboated.backend.web.ganttchart;

import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.AccountTask;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.web.config.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.common.data.BasicDataTask.TASK_ID;
import static com.projectboated.backend.web.ganttchart.document.GanttChartDocument.documentMyGanttChartRetrieve;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("GanttChart : Controller 단위테스트")
class GanttChartControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void GetMyGanttChart_조회_조회정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Kanban kanban = createKanban(KANBAN_ID, project);
        KanbanLane kanbanLane = createKanbanLane(KANBAN_LANE_ID, project, kanban);
        Task task = createTask(TASK_ID, project, kanbanLane);
        AccountTask accountTask = createAccountTask(account, task);

        when(projectService.findByAccountIdAndDate(any(), any())).thenReturn(List.of(project));
        when(accountTaskService.findByProjectIdAndAccountId(any(), any())).thenReturn(List.of(accountTask));

        // When
        // Then
        mockMvc.perform(get( "/api/my/gantt-chart"))
                .andExpect(status().isOk())
                .andDo(documentMyGanttChartRetrieve());
    }

}