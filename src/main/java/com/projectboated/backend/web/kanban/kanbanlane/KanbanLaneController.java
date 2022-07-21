package com.projectboated.backend.web.kanban.kanbanlane;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanbanlane.service.KanbanLaneService;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.ChangeTaskOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
