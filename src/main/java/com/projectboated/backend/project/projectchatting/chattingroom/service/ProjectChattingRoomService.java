package com.projectboated.backend.project.projectchatting.chattingroom.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.project.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.project.projectchatting.chatting.domain.Chatting;
import com.projectboated.backend.project.projectchatting.chatting.repository.ChattingRepository;
import com.projectboated.backend.project.projectchatting.chattingroom.domain.ProjectChattingRoom;
import com.projectboated.backend.project.projectchatting.chattingroom.repository.ProjectChattingRoomRepository;
import com.projectboated.backend.project.projectchatting.chattingroom.service.exception.ProjectChattingRoomNotFoundException;
import com.projectboated.backend.project.projectchatting.chattingroom.websocket.dto.CreateChattingMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectChattingRoomService {

    private final AccountRepository accountRepository;
    private final ProjectChattingRoomRepository projectChattingRoomRepository;
    private final ChattingRepository chattingRepository;

    @OnlyCaptainOrCrew
    public Chatting saveChatting(Long accountId, Long projectId, CreateChattingMessageRequest chattingMessage) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        ProjectChattingRoom projectChattingRoom = projectChattingRoomRepository.findByProjectId(projectId)
                .orElseThrow(ProjectChattingRoomNotFoundException::new);

        Chatting chatting = Chatting.builder()
                .chattingRoom(projectChattingRoom)
                .account(account)
                .body(chattingMessage.getBody())
                .build();

        return chattingRepository.save(chatting);
    }
}
