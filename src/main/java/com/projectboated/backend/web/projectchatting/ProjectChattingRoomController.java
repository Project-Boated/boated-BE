package com.projectboated.backend.web.projectchatting;

import com.projectboated.backend.domain.projectchatting.chatting.service.ChattingService;
import com.projectboated.backend.domain.projectchatting.projectchattingroom.service.ProjectChattingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/chatting")
public class ProjectChattingRoomController {

    private final ProjectChattingRoomService projectChattingRoomService;
    private final ChattingService chattingService;

    @GetMapping("/room")
    public void getProjectChattingRoom(
            @PathVariable Long projectId
    ) {
//        ProjectChattingRoom projectChattingRoom = projectChattingRoomService.findByProjectId(projectId);
//        return new GetProjectChattingRoomResponse(projectChattingRoom.getId(),
//                chattingService.findByChattingRoom());
    }

}
