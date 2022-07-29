package com.projectboated.backend.web.task.tasklike;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.task.tasklike.service.TaskLikeService;
import com.projectboated.backend.web.invitation.dto.response.AcceptInvitationResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskLikeController {

    private final TaskLikeService taskLikeService;

    @PostMapping("/api/projects/{projectId}/tasks/{taskId}/like")
    public void likeTask(
            @AuthenticationPrincipal Account account,
            @PathVariable Long projectId,
            @PathVariable Long taskId
            ) {
        taskLikeService.likeTask(account.getId(), projectId, taskId);
    }
	
}
