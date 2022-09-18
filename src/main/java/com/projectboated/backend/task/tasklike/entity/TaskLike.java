package com.projectboated.backend.task.tasklike.entity;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.common.entity.BaseTimeEntity;
import com.projectboated.backend.task.task.entity.Task;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "order_index")
    private Integer order;

    @Builder
    public TaskLike(Long id, Account account, Task task, Integer order) {
        this.id = id;
        this.account = account;
        this.task = task;
        this.order = order;
    }

    public void changeOrder(Integer order) {
        this.order = order;
    }
}
