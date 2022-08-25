package com.projectboated.backend.web.projectvideo;

import com.projectboated.backend.domain.projectvideo.service.ProjectVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/projects/{projectId}/video")
@RequiredArgsConstructor
public class ProjectVideoController {

    private final ProjectVideoService projectVideoService;

    @PostMapping
    public void createProjectVideo(
            @PathVariable Long projectId,
            @RequestParam MultipartFile file
            ) {
        projectVideoService.save(projectId, file);
    }
}
