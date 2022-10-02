package com.projectboated.backend.project.projectchatting.chattingroom.controller;

import com.projectboated.backend.project.projectchatting.chatting.service.ChattingService;
import com.projectboated.backend.project.projectchatting.chattingroom.service.ChattingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/chatting")
public class ProjectChattingRoomController {

    private final ChattingRoomService projectChattingRoomService;
    private final ChattingService chattingService;

    @GetMapping
    public void getProjectChattingRoom(
            @PathVariable Long projectId
    ) {
//        ProjectChattingRoom projectChattingRoom = projectChattingRoomService.findByProjectId(projectId);
//        return new GetProjectChattingRoomResponse(projectChattingRoom.getId(),
//                chattingService.findByChattingRoom());
    }

}
