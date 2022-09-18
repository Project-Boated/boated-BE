package com.projectboated.backend.projectvideo.repository;

import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.projectvideo.entity.ProjectVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectVideoRepository extends JpaRepository<ProjectVideo, Long> {
    Optional<ProjectVideo> findByProject(Project project);
}
