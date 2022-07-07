package com.projectboated.backend.web.kanban.kanban;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanban.service.KanbanService;
import com.projectboated.backend.web.kanban.kanban.dto.response.GetKanbanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanService kanbanService;

    @GetMapping("/api/projects/{projectId}/kanban")
    public GetKanbanResponse getKanban(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId
    ) {
        Kanban kanban = kanbanService.find(account, projectId);
        return new GetKanbanResponse(kanban.getLanes());
    }
}
