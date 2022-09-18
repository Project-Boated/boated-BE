package com.projectboated.backend.projectchatting.chattingroom.domain;

import com.projectboated.backend.project.entity.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@DiscriminatorValue("project")
public class ProjectChattingRoom extends ChattingRoom {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public ProjectChattingRoom(Long id, Project project) {
        super(id);
        this.project = project;
    }

}
