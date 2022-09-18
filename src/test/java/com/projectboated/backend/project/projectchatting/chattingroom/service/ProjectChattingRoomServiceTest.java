package com.projectboated.backend.project.projectchatting.chattingroom.service;

import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.project.project.repository.ProjectRepository;
import com.projectboated.backend.project.projectchatting.chatting.repository.ChattingRepository;
import com.projectboated.backend.project.projectchatting.chattingroom.repository.ProjectChattingRoomRepository;
import com.projectboated.backend.project.projectchatting.chattingroom.service.ProjectChattingRoomService;
import com.projectboated.backend.utils.base.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("ProjectChattingRoom : Service 단위 테스트")
class ProjectChattingRoomServiceTest extends ServiceTest {

    @InjectMocks
    ProjectChattingRoomService projectChattingRoomService;

    @Mock
    AccountRepository accountRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    ProjectChattingRoomRepository projectChattingRoomRepository;
    @Mock
    ChattingRepository chattingRepository;

    @Test
    void saveChatting_찾을수없는project_예외발생() {
        // Given

        // When


        // Then

    }

}