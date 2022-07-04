package com.projectboated.backend.web.kanban.kanban;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.task.entity.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.kanban.kanban.service.KanbanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanService kanbanService;

    @GetMapping("/api/projects/{projectId}/kanban")
    public ResponseEntity<GetKanbanResponse> getKanban(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId
    ) {
        Kanban kanban = kanbanService.find(account, projectId);
        return ResponseEntity.ok(new GetKanbanResponse(kanban.getLanes()));
    }

    @Getter
    public static class GetKanbanResponse {

        private List<KanbanLaneResponse> lanes;

        public GetKanbanResponse(List<KanbanLane> lanes) {
            this.lanes = lanes.stream().map(KanbanLaneResponse::new).toList();
        }

        @Getter
        static class KanbanLaneResponse {

            private String name;
            private List<TaskResponse> tasks;

            public KanbanLaneResponse(KanbanLane kl) {
                this.name = kl.getName();
                this.tasks = kl.getTasks().stream().map(t -> new TaskResponse(t)).toList();
            }
        }

        @Getter
        static class TaskResponse {

            private Long id;
            private String name;
            private String description;
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            private LocalDateTime deadline;
            private Integer dday;
            private int fileCount;

            public TaskResponse(Task task) {
                this.id = task.getId();
                this.name = task.getName();
                this.description = task.getDescription();
                this.deadline = task.getDeadline();
                this.dday = task.getDeadline()!=null ? Period.between(deadline.toLocalDate(), LocalDate.now()).getDays() : null;
                this.fileCount = 0;
            }
        }
    }
}
