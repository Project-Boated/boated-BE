package com.projectboated.backend.projectchatting.chattingroom.controller;

import com.projectboated.backend.projectchatting.chatting.service.ChattingService;
import com.projectboated.backend.projectchatting.chattingroom.service.ChattingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
