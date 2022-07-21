package com.projectboated.backend.domain.kanban.kanbanlane.entity;

import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.exception.TaskChangeIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.exception.TaskOriginalIndexOutOfBoundsException;
import com.projectboated.backend.domain.task.entity.Task;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KanbanLane extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_lane_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id")
    private Kanban kanban;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kanbanLane",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    @OrderColumn(name = "task_index")
    private List<Task> tasks = new ArrayList<>();

    @Builder
    public KanbanLane(String name, Kanban kanban) {
        this.name = name;
        this.kanban = kanban;
    }

    public void changeKanban(Kanban kanban) {
        this.kanban = kanban;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        task.changeKanbanLane(this);
    }

    public void addTask(int index, Task task) {
        if (index < 0 || index > tasks.size()) {
            throw new TaskChangeIndexOutOfBoundsException(ErrorCode.TASK_CHANGE_INDEX_OUT_OF_BOUNDS);
        }
        this.tasks.add(index, task);
        task.changeKanbanLane(this);
    }

    public Task removeTask(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new TaskOriginalIndexOutOfBoundsException(ErrorCode.TASK_ORIGINAL_INDEX_OUT_OF_BOUNDS);
        }
        return tasks.remove(index);
    }

    public void changeTaskOrder(int originalIndex, int changeIndex) {
        Task task = this.removeTask(originalIndex);
        this.addTask(changeIndex, task);
    }
}
