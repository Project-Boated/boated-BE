package com.projectboated.backend.web.kanban.kanbanlane;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanbanlane.service.KanbanLaneService;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.ChangeTaskOrderRequest;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.KanbanLaneUpdateRequest;
import com.projectboated.backend.web.kanban.kanbanlane.dto.UpdateKanbanLaneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/kanban/lanes")
public class KanbanLaneController {

    private final KanbanLaneService kanbanLaneService;

    @PostMapping("/tasks/change/{originalLaneId}/{originalTaskIndex}/{changeLaneId}/{changeTaskIndex}")
    public void changeTaskOrder(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long originalLaneId,
            @PathVariable int originalTaskIndex,
            @PathVariable Long changeLaneId,
            @PathVariable int changeTaskIndex
    ) {
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .projectId(projectId)
                .originalLaneId(originalLaneId)
                .originalTaskIndex(originalTaskIndex)
                .changeLaneId(changeLaneId)
                .changeTaskIndex(changeTaskIndex)
                .build();
        kanbanLaneService.changeTaskOrder(account.getId(), request);
    }

    @PutMapping("/{kanbanLaneId}")
    public void updateKanbanLane(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long kanbanLaneId,
            @RequestBody UpdateKanbanLaneRequest request
    ) {
        KanbanLaneUpdateRequest kluRequest = KanbanLaneUpdateRequest.builder()
                .projectId(projectId)
                .kanbanLaneId(kanbanLaneId)
                .name(request.getName())
                .build();

        kanbanLaneService.updateKanbanLane(account.getId(), kluRequest);
    }

}
