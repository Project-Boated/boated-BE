package org.projectboated.backend.web.kanban.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.projectboated.backend.domain.kanban.entity.KanbanLane;
import org.projectboated.backend.domain.task.entity.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Getter
public class GetKanbanResponse {

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
