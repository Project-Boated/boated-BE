package com.projectboated.backend.domain.projectchatting.projectchattingroom.domain;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ChattingRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor @Getter
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
