package com.projectboated.backend.project.projectchatting.chattingroom.repository;

import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.projectchatting.chattingroom.domain.ProjectChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectChattingRoomRepository extends JpaRepository<ProjectChattingRoom, Long> {
    Optional<ProjectChattingRoom> findByProject(Project project);
}
