package org.projectboated.backend.web.kanban;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.kanban.entity.CustomKanbanLane;
import org.projectboated.backend.domain.kanban.entity.Kanban;
import org.projectboated.backend.domain.kanban.entity.KanbanLane;
import org.projectboated.backend.domain.kanban.service.KanbanLaneService;
import org.projectboated.backend.domain.kanban.service.KanbanService;
import org.projectboated.backend.web.kanban.dto.CreateKanbanLaneRequest;
import org.projectboated.backend.web.kanban.dto.GetKanbanResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanService kanbanService;
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

    @GetMapping("/api/projects/{projectId}/kanban")
    public ResponseEntity<GetKanbanResponse> getKanban(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId
    ) {
        Kanban kanban = kanbanService.find(account, projectId);
        return ResponseEntity.ok(new GetKanbanResponse(kanban.getLanes()));
    }
}
