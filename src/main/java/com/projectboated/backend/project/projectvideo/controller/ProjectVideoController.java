package com.projectboated.backend.project.projectvideo.controller;

import com.projectboated.backend.infra.aws.AwsS3Service;
import com.projectboated.backend.project.projectvideo.controller.dto.response.GetDescriptionResponse;
import com.projectboated.backend.project.projectvideo.controller.dto.request.PutDescriptionRequest;
import com.projectboated.backend.project.projectvideo.entity.ProjectVideo;
import com.projectboated.backend.project.projectvideo.service.ProjectVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/projects/{projectId}/video")
@RequiredArgsConstructor
public class ProjectVideoController {

    private final ProjectVideoService projectVideoService;
    private final AwsS3Service awsS3Service;

    @PutMapping
    public void putProjectVideo(
            @PathVariable Long projectId,
            @RequestParam MultipartFile file
    ) {
        projectVideoService.save(projectId, file);
    }

    @GetMapping
    public ResponseEntity<byte[]> getProjectVideo(@PathVariable Long projectId) {
        ProjectVideo projectVideo = projectVideoService.findByProjectId(projectId);
        byte[] body = awsS3Service.getBytes(projectVideo.getKey());
        return ResponseEntity.
                ok()
                .contentType(MediaType.valueOf(projectVideo.getUploadFile().getMediaType()))
                .contentLength(body.length)
                .body(body);
    }

    @PutMapping("/description")
    public void putDescription(
            @PathVariable Long projectId,
            @RequestBody PutDescriptionRequest request
    ) {
        projectVideoService.updateDescription(projectId, request.getDescription());
    }

    @GetMapping("/description")
    public GetDescriptionResponse getDescription(
            @PathVariable Long projectId
    ) {
        String description = projectVideoService.getDescription(projectId);
        return new GetDescriptionResponse(description);
    }

    @DeleteMapping
    public void deleteProjectVideo(@PathVariable Long projectId) {
        projectVideoService.deleteByProjectId(projectId);
    }
}
