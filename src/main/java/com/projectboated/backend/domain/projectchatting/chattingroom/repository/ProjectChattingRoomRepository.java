package com.projectboated.backend.domain.projectchatting.chattingroom.repository;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ProjectChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectChattingRoomRepository extends JpaRepository<ProjectChattingRoom, Long> {
    Optional<ProjectChattingRoom> findByProject(Project project);
}
