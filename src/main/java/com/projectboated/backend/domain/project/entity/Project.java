package com.projectboated.backend.domain.project.entity;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.project.service.condition.ProjectUpdateCond;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    private String name;

    private String description;

    private LocalDateTime deadline;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id_captain")
    private Account captain;

    @OneToOne(mappedBy = "project",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private Kanban kanban;

    private boolean isTerminated;

    @Builder
    public Project(Long id, Account captain, String name, String description, LocalDateTime deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.captain = captain;
        this.isTerminated = false;
    }

    public void changeName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public void changeDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public void changeDeadline(LocalDateTime deadLine) {
        if (deadLine != null) {
            this.deadline = deadLine;
        }
    }

    public void changeCaptain(Account account) {
        this.captain = account;
    }

    public void changeKanban(Kanban kanban) {
        if (this.kanban == null || !this.kanban.equals(kanban)) {
            this.kanban = kanban;
            kanban.changeProject(this);
        }
    }

    public void terminate() {
        isTerminated = true;
    }

    public void cancelTerminate() {
        isTerminated = false;
    }

    public boolean isCaptain(Account captain) {
        return this.captain.getId() == captain.getId();
    }

    public void update(ProjectUpdateCond cond) {
        this.changeName(cond.getName());
        this.changeDescription(cond.getDescription());
        this.changeDeadline(cond.getDeadline());
    }
}
