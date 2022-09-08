package com.projectboated.backend.domain.projectchatting.chattingroom.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.AccountService;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.projectchatting.chatting.domain.Chatting;
import com.projectboated.backend.domain.projectchatting.chatting.repository.ChattingRepository;
import com.projectboated.backend.domain.projectchatting.chattingroom.domain.ProjectChattingRoom;
import com.projectboated.backend.domain.projectchatting.chattingroom.repository.ProjectChattingRoomRepository;
import com.projectboated.backend.domain.projectchatting.chattingroom.service.exception.ProjectChattingRoomNotFoundException;
import com.projectboated.backend.websocket.projectchatting.dto.ChattingMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectChattingRoomService {

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final ProjectChattingRoomRepository projectChattingRoomRepository;
    private final ChattingRepository chattingRepository;

    @OnlyCaptainOrCrew
    public void saveChatting(Long projectId, Long accountId, ChattingMessage chattingMessage) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        ProjectChattingRoom projectChattingRoom = projectChattingRoomRepository.findByProject(project)
                .orElseThrow(ProjectChattingRoomNotFoundException::new);

        Chatting chatting = Chatting.builder()
                .chattingRoom(projectChattingRoom)
                .account(account)
                .body(chattingMessage.getBody())
                .build();

        chattingRepository.save(chatting);
    }
}
