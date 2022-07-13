package com.projectboated.backend.web.kanban.kanbanlane;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.CustomKanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.service.KanbanLaneService;
import com.projectboated.backend.web.kanban.kanbanlane.dto.request.CreateKanbanLaneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class KanbanLaneController {

    private final KanbanLaneService kanbanLaneService;

    @PostMapping(value = "/api/projects/{projectId}/kanban/lanes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createCustomKanbanLane(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody CreateKanbanLaneRequest request
    ) {
        KanbanLane kanbanLane = CustomKanbanLane.builder()
                .name(request.getName())
                .build();

        kanbanLaneService.save(account, projectId, kanbanLane);
    }

    @DeleteMapping("/api/projects/{projectId}/kanban/lanes")
    public void deleteCustomKanbanLane(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId
    ) {
        kanbanLaneService.deleteCustomLane(account, projectId);
    }

    @PostMapping("/api/projects/{projectId}/kanban/lanes/change/{originalIndex}/{changeIndex}")
    public void changeKanbanLaneIndex(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable int originalIndex,
            @PathVariable int changeIndex
    ) {
        kanbanLaneService.changeKanbanLaneIndex(account, projectId, originalIndex, changeIndex);
    }

}
