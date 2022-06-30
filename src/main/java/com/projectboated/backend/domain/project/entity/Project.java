package com.projectboated.backend.domain.project.entity;

import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.kanban.entity.Kanban;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    private String name;

    private String description;

    private LocalDateTime deadline;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id_captain")
    private Account captain;

    @OneToOne(mappedBy = "project")
    private Kanban kanban;

    private boolean isTerminated;

    @Builder
    public Project(String name, String description, Account captain, LocalDateTime deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.captain = captain;
        this.isTerminated = false;
    }

    public void changeProjectInform(String name, String description, LocalDateTime deadline) {
        if (name != null) {
            this.name = name;
        }
        if (description != null) {
            this.description = description;
        }
        if (deadline != null) {
            this.deadline = deadline;
        }
    }

    public void changeCaptain(Account account) {
        this.captain = account;
    }

    public void changeKanban(Kanban kanban) {
        if (!this.kanban.equals(kanban)) {
            this.kanban = kanban;
            kanban.changeProject(this);
        }
    }

    public void terminateProject() {
        isTerminated = true;
    }

    public void cancelTerminateProject() {
        isTerminated = false;
    }
}