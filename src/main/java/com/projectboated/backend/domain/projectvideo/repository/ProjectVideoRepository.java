package com.projectboated.backend.domain.projectvideo.repository;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.projectvideo.entity.ProjectVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectVideoRepository extends JpaRepository<ProjectVideo, Long> {
    Optional<ProjectVideo> findByProject(Project project);
}
