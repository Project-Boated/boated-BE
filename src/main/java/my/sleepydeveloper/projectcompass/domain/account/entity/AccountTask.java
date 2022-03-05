package my.sleepydeveloper.projectcompass.domain.account.entity;

import my.sleepydeveloper.projectcompass.domain.common.entity.BaseTimeEntity;
import my.sleepydeveloper.projectcompass.domain.task.entity.Task;

import javax.persistence.*;

@Entity
public class AccountTask extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_task_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;
}
