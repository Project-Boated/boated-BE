package com.projectboated.backend.project.projectvideo.repository;

import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.projectvideo.entity.ProjectVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectVideoRepository extends JpaRepository<ProjectVideo, Long> {
    Optional<ProjectVideo> findByProject(Project project);
}
