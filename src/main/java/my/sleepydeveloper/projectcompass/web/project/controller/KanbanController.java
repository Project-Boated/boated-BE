package my.sleepydeveloper.projectcompass.web.project.controller;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.kanban.entity.KanbanLane;
import my.sleepydeveloper.projectcompass.domain.kanban.service.KanbanLaneService;
import my.sleepydeveloper.projectcompass.web.project.dto.CreateKanbanLaneRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanLaneService kanbanLaneService;

    @PostMapping("/api/projects/{projectId}/kanban/lanes")
    public void createKanbanLane(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @RequestBody CreateKanbanLaneRequest createKanbanLaneRequest
            ) {
        KanbanLane kanbanLane = KanbanLane.builder()
                .name(createKanbanLaneRequest.getName())
                .build();

        kanbanLaneService.save(account, projectId, kanbanLane);
    }
}
