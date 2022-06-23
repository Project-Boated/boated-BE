package org.projectboated.backend.web.kanban;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.kanban.entity.CustomKanbanLane;
import org.projectboated.backend.domain.kanban.entity.KanbanLane;
import org.projectboated.backend.domain.kanban.service.KanbanLaneService;
import org.projectboated.backend.web.kanban.dto.CreateKanbanLaneRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanLaneService kanbanLaneService;

    @PostMapping(value = "/api/projects/{projectId}/kanban/lanes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createCustomKanbanLane(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody CreateKanbanLaneRequest createKanbanLaneRequest
            ) {
        KanbanLane kanbanLane = CustomKanbanLane.builder()
                .name(createKanbanLaneRequest.getName())
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
}
