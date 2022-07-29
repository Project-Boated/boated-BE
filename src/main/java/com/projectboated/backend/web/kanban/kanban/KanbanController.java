package com.projectboated.backend.web.kanban.kanban;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanban.service.KanbanService;
import com.projectboated.backend.domain.kanban.kanbanlane.service.KanbanLaneService;
import com.projectboated.backend.web.kanban.kanban.dto.response.GetKanbanResponse;
import com.projectboated.backend.web.kanban.kanbanlane.dto.request.CreateKanbanLaneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/kanban")
public class KanbanController {

    private final KanbanService kanbanService;
    private final KanbanLaneService kanbanLaneService;

    @GetMapping
    public GetKanbanResponse getKanban(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId
    ) {
        Kanban kanban = kanbanService.findByProjectId(account.getId(), projectId);
        return new GetKanbanResponse(kanban.getLanes());
    }

    @PostMapping(value = "/lanes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createKanbanLane(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody CreateKanbanLaneRequest request
    ) {
        kanbanLaneService.createNewLine(account.getId(), projectId, request.getName());
    }

    @DeleteMapping("/lanes/{kanbanLaneId}")
    public void deleteKanbanLane(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long kanbanLaneId
    ) {
        kanbanLaneService.deleteKanbanLane(account.getId(), projectId, kanbanLaneId);
    }

    @PostMapping("/lanes/change/{originalIndex}/{changeIndex}")
    public void changeKanbanLaneOrder(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable int originalIndex,
            @PathVariable int changeIndex
    ) {
        kanbanService.changeKanbanLaneOrder(account.getId(), projectId, originalIndex, changeIndex);
    }

}
