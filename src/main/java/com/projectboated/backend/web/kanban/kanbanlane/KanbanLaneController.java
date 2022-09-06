package com.projectboated.backend.web.kanban.kanbanlane;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.service.KanbanLaneService;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.KanbanLaneUpdateRequest;
import com.projectboated.backend.web.kanban.kanbanlane.dto.GetLanesResponse;
import com.projectboated.backend.web.kanban.kanbanlane.dto.TaskLaneChangeMessage;
import com.projectboated.backend.web.kanban.kanbanlane.dto.UpdateKanbanLaneRequest;
import com.projectboated.backend.web.kanban.kanbanlane.dto.request.CreateKanbanLaneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/lanes")
public class KanbanLaneController {

    private final KanbanLaneService kanbanLaneService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public GetLanesResponse getLanes(
            @PathVariable Long projectId
    ) {
        List<KanbanLane> lanes = kanbanLaneService.findByProjectId(projectId);
        return new GetLanesResponse(lanes);
    }

    @PutMapping("/{kanbanLaneId}")
    public void updateKanbanLane(
            @PathVariable Long projectId,
            @PathVariable Long kanbanLaneId,
            @RequestBody UpdateKanbanLaneRequest request
    ) {
        KanbanLaneUpdateRequest kluRequest = KanbanLaneUpdateRequest.builder()
                .name(request.getName())
                .build();

        kanbanLaneService.updateKanbanLane(projectId, kanbanLaneId, kluRequest);
    }

    @PostMapping("/change/{originalIndex}/{changeIndex}")
    public void changeKanbanLaneOrder(
            @PathVariable Long projectId,
            @PathVariable int originalIndex,
            @PathVariable int changeIndex
    ) {
        kanbanLaneService.changeKanbanLaneOrder(projectId, originalIndex, changeIndex);
        messagingTemplate.convertAndSend("/topic/projects/{projectId}/lanes/change", new TaskLaneChangeMessage(originalIndex, changeIndex));
    }

    @PostMapping
    public void createKanbanLane(
            @PathVariable Long projectId,
            @RequestBody CreateKanbanLaneRequest request
    ) {
        kanbanLaneService.createNewLine(projectId, request.getName());
    }

    @DeleteMapping("/{kanbanLaneId}")
    public void deleteKanbanLane(
            @PathVariable Long projectId,
            @PathVariable Long kanbanLaneId
    ) {
        kanbanLaneService.deleteKanbanLane(projectId, kanbanLaneId);
    }


}
