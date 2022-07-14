package com.projectboated.backend.domain.kanban.kanbanlane.entity;

import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.exception.TaskChangeIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.exception.TaskOriginalIndexOutOfBoundsException;
import com.projectboated.backend.domain.task.entity.Task;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
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

    protected KanbanLane(String name, Kanban kanban) {
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

    public void changeTaskOrder(int originalIndex, int changeIndex) {
        if (originalIndex < 0 || originalIndex >= tasks.size()) {
            throw new TaskOriginalIndexOutOfBoundsException(ErrorCode.TASK_ORIGINAL_INDEX_OUT_OF_BOUNDS);
        }
        if (changeIndex < 0 || changeIndex >= tasks.size()) {
            throw new TaskChangeIndexOutOfBoundsException(ErrorCode.TASK_CHANGE_INDEX_OUT_OF_BOUNDS);
        }

        Task task = tasks.remove(originalIndex);
        if (changeIndex == tasks.size()) {
            this.tasks.add(task);
        } else {
            this.tasks.add(changeIndex, task);
        }
    }
}
