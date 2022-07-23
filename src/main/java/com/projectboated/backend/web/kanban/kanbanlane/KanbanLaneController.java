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
public class KanbanLaneController {

    private final KanbanLaneService kanbanLaneService;

    @PostMapping("/api/projects/{projectId}/kanban/lanes/tasks/change/{originalLaneIndex}/{originalTaskIndex}/{changeLaneIndex}/{changeTaskIndex}")
    public void changeTaskOrder(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable int originalLaneIndex,
            @PathVariable int originalTaskIndex,
            @PathVariable int changeLaneIndex,
            @PathVariable int changeTaskIndex
    ) {
        ChangeTaskOrderRequest request = ChangeTaskOrderRequest.builder()
                .projectId(projectId)
                .originalLaneIndex(originalLaneIndex)
                .originalTaskIndex(originalTaskIndex)
                .changeLaneIndex(changeLaneIndex)
                .changeTaskIndex(changeTaskIndex)
                .build();
        kanbanLaneService.changeTaskOrder(account.getId(), request);
    }

    @PutMapping("/api/projects/{projectId}/kanban/lanes/{kanbanLaneIndex}")
    public void updateKanbanLane(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable int kanbanLaneIndex,
            @RequestBody UpdateKanbanLaneRequest request
    ) {
        KanbanLaneUpdateRequest kluRequest = KanbanLaneUpdateRequest.builder()
                .projectId(projectId)
                .kanbanLaneIndex(kanbanLaneIndex)
                .name(request.getName())
                .build();

        kanbanLaneService.updateKanbanLane(account.getId(), kluRequest);
    }

}
