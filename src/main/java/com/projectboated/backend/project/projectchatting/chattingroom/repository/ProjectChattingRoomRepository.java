package com.projectboated.backend.project.projectchatting.chattingroom.repository;

import com.projectboated.backend.project.projectchatting.chattingroom.domain.ProjectChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectChattingRoomRepository extends JpaRepository<ProjectChattingRoom, Long> {
    @Query("select pcr from ProjectChattingRoom pcr where pcr.project.id=:projectId")
    Optional<ProjectChattingRoom> findByProjectId(@Param("projectId") Long projectId);
}
