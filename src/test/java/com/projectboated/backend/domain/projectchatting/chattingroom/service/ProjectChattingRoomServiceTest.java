package com.projectboated.backend.domain.projectchatting.chattingroom.service;

import com.projectboated.backend.utils.base.ServiceTest;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.projectchatting.chatting.repository.ChattingRepository;
import com.projectboated.backend.domain.projectchatting.chattingroom.repository.ProjectChattingRoomRepository;
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