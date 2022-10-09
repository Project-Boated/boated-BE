package com.projectboated.backend.project.projectvideo.repository;

import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.projectvideo.entity.ProjectVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectVideoRepository extends JpaRepository<ProjectVideo, Long> {
    Optional<ProjectVideo> findByProject(Project project);

    @Query("select pv from ProjectVideo pv where pv.project.id=:projectId")
    Optional<ProjectVideo> findByProjectId(@Param("projectId") Long projectId);
}
