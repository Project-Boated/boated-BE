package com.projectboated.backend.project.entity;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.common.entity.BaseTimeEntity;
import com.projectboated.backend.project.entity.exception.ProjectTotalFileSizeIsMinusException;
import com.projectboated.backend.project.service.condition.ProjectUpdateCond;
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

    private boolean isTerminated;

    private Long totalFileSize;

    @Builder
    public Project(Long id, Account captain, String name, String description, LocalDateTime deadline) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.captain = captain;
        this.isTerminated = false;
        this.totalFileSize = 0L;
    }

    public void update(ProjectUpdateCond cond) {
        this.changeName(cond.getName());
        this.changeDescription(cond.getDescription());
        this.changeDeadline(cond.getDeadline());
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

    public boolean isCaptain(Account captain) {
        return this.captain.getId() == captain.getId();
    }

    public void changeCaptain(Account account) {
        this.captain = account;
    }

    public void terminate() {
        isTerminated = true;
    }

    public void cancelTerminate() {
        isTerminated = false;
    }

    public void addTotalFileSize(Long fileSize) {
        totalFileSize += fileSize;
    }

    public void minusTotalFileSize(Long fileSize) {
        if (totalFileSize - fileSize < 0) {
            throw new ProjectTotalFileSizeIsMinusException();
        }
        totalFileSize -= fileSize;
    }
}
