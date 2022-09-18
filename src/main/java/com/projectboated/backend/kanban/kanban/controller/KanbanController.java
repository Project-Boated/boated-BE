package com.projectboated.backend.kanban.kanban.controller;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.task.task.entity.AccountTask;
import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.task.service.AccountTaskService;
import com.projectboated.backend.task.task.service.TaskService;
import com.projectboated.backend.task.tasklike.service.TaskLikeService;
import com.projectboated.backend.kanban.kanban.controller.response.GetKanbanResponse;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanban.service.KanbanService;
import com.projectboated.backend.kanban.kanbanlane.controller.dto.common.KanbanLaneResponse;
import com.projectboated.backend.kanban.kanbanlane.controller.dto.request.CreateKanbanLaneRequest;
import com.projectboated.backend.kanban.kanbanlane.service.KanbanLaneService;
import com.projectboated.backend.task.task.controller.dto.common.TaskAssignedAccountResponse;
import com.projectboated.backend.task.task.controller.dto.common.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/kanban")
public class KanbanController {

    private final KanbanService kanbanService;
    private final KanbanLaneService kanbanLaneService;
    private final TaskService taskService;
    private final TaskLikeService taskLikeService;
    private final AccountTaskService accountTaskService;

    @GetMapping
    public GetKanbanResponse getKanban(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId
    ) {
        Map<Task, Boolean> taskLikeMap = taskLikeService.findByProjectAndAccount(account.getId(), projectId);

        Kanban kanban = kanbanService.findByProjectId(projectId);
        List<KanbanLaneResponse> kanbanLaneResponses = kanbanLaneService.findByProjectId(projectId).stream()
                .map(kl -> new KanbanLaneResponse(kl, taskService.findByProjectIdAndKanbanLaneId(projectId, kl.getId()).stream()
                        .map((t) -> new TaskResponse(t, taskLikeMap, accountTaskService.findByTask(projectId, t.getId()).stream()
                                .map(AccountTask::getAccount)
                                .map(TaskAssignedAccountResponse::new)
                                .toList()))
                        .toList()))
                .toList();

        return new GetKanbanResponse(kanban, kanbanLaneResponses);
    }

    @PostMapping(value = "/lanes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createKanbanLane(
            @PathVariable Long projectId,
            @RequestBody CreateKanbanLaneRequest request
    ) {
        kanbanLaneService.createNewLine(projectId, request.getName());
    }

    @DeleteMapping("/lanes/{kanbanLaneId}")
    public void deleteKanbanLane(
            @PathVariable Long projectId,
            @PathVariable Long kanbanLaneId
    ) {
        kanbanLaneService.deleteKanbanLane(projectId, kanbanLaneId);
    }

}
