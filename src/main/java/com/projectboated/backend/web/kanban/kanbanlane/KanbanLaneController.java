package com.projectboated.backend.web.kanban.kanbanlane;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanbanlane.service.KanbanLaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KanbanLaneController {

    private final KanbanLaneService kanbanLaneService;

    @PostMapping("/api/projects/{projectId}/kanban/lanes/{laneId}/tasks/change/{originalIndex}/{changeIndex}")
    public void changeTaskOrder(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long laneId,
            @PathVariable int originalIndex,
            @PathVariable int changeIndex
    ) {
        kanbanLaneService.changeTaskOrder(account, projectId, laneId, originalIndex, changeIndex);
    }

}
